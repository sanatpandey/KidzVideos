import axios from "axios";
import React, { useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { CircularProgressbar, buildStyles } from "react-circular-progressbar";
import 'react-circular-progressbar/dist/styles.css';

const UploadVideo = () => {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [ageGroup, setAgeGroup] = useState('');
  const [genre, setGenre] = useState('');
  const [isTrending, setIsTrending] = useState('false');
  const [file, setFile] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [showSuccessDialog, setShowSuccessDialog] = useState(false);
  const [uploadProgress, setUploadProgress] = useState(0);
  const [isUploading, setIsUploading] = useState(false);

  const fileInputRef = useRef(null);
  const navigate = useNavigate();

  const resetForm = () => {
    setName('');
    setDescription('');
    setAgeGroup('');
    setGenre('');
    setIsTrending('false');
    setFile('');
    setError('');
    setSuccess('');
    setUploadProgress(0);
    setIsUploading(false);
    setShowSuccessDialog(false);
    if (fileInputRef.current) {
      fileInputRef.current.value = null;
    }
  };

  const handleUpload = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setUploadProgress(0);
    setIsUploading(true);

    if (!file) {
      setError('Please select a video file');
      setIsUploading(false);
      return;
    }

    const formData = new FormData();
    const metadata = {
      name,
      description,
      ageGroup,
      genre,
      isTrending,
    };

    formData.append('file', file);
    formData.append('data', new Blob([JSON.stringify(metadata)], { type: 'application/json' }));

    try {
      const token = localStorage.getItem('authToken');
      await axios.post('http://localhost:8082/video/save', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
          'Authorization': `Bearer ${token}`
        },
        onUploadProgress: (progressEvent) => {
          const percent = Math.round((progressEvent.loaded * 100) / progressEvent.total);
          setUploadProgress(percent);
        }
      });
      setSuccess('Video Uploaded Successfully!');
      setShowSuccessDialog(true);
    } catch (err) {
      console.error(err);
      setError('Upload Failed. Check input or permissions.');
    } finally {
      setIsUploading(false);
    }
  };

  return (
    <>
      {/* Upload Form */}
      <div className="min-h-screen flex justify-center items-center bg-gradient-to-br from-yellow-100 via-pink-100 to-purple-100">
        <form onSubmit={handleUpload} className="bg-white p-8 rounded-3xl shadow-3xl w-full max-w-lg border border-pink-300">
          <h2 className="text-2xl font-bold text-pink-500 text-center mb-4">Upload New Video</h2>

          <input
            type="text"
            placeholder="Video Name"
            value={name}
            onChange={(e) => setName(e.target.value)}
            className="w-full p-2 border border-gray-300 rounded mt-2"
            required
          />

          <textarea
            placeholder="Description"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            rows={4}
            className="w-full p-2 border border-gray-300 rounded mt-3"
          />

          <select
            value={ageGroup}
            onChange={(e) => setAgeGroup(e.target.value)}
            className="w-full p-2 border border-gray-300 rounded mt-3"
            required
          >
            <option value="">Select Age Group</option>
            <option value="lt5">Less than 5</option>
            <option value="lt10">Less than 10</option>
            <option value="lt15">Less than 15</option>
          </select>

          <input
            type="text"
            placeholder="Genre (e.g. Cartoon, Education)"
            value={genre}
            onChange={(e) => setGenre(e.target.value)}
            className="w-full p-2 border border-gray-300 rounded mt-3"
            required
          />

          <input
            type="file"
            accept="video/*"
            ref={fileInputRef}
            onChange={(e) => setFile(e.target.files[0])}
            className="w-full mt-3"
            required
          />

          {error && <p className="text-red-500 mt-2 text-sm">{error}</p>}
          {success && <p className="text-green-500 mt-2 text-sm">{success}</p>}

          <button
            type="submit"
            className="w-full mt-5 bg-pink-500 hover:bg-pink-600 text-white font-semibold py-2 rounded"
            disabled={isUploading}
          >
            {isUploading ? 'Uploading...' : success ? 'Uploaded!' : 'Upload Video'}
          </button>
        </form>
      </div>

      {/* Circular Overlay Loader */}
      {isUploading && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-40 z-50">
          <div className="w-32 h-32">
            <CircularProgressbar
              value={uploadProgress}
              text={`${uploadProgress}%`}
              styles={buildStyles({
                pathColor: "#f472b6",
                textColor: "#f472b6",
                trailColor: "#ffe4e6",
              })}
            />
          </div>
        </div>
      )}

      {/* Success Dialog */}
      {showSuccessDialog && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-40 z-50">
          <div className="bg-white p-6 rounded-lg shadow-lg text-center z-50">
            <h3 className="text-xl font-semibold text-green-600 mb-4">{success}</h3>
            <button
              className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
              onClick={resetForm}
            >
              OK
            </button>
          </div>
        </div>
      )}
    </>
  );
};

export default UploadVideo;
