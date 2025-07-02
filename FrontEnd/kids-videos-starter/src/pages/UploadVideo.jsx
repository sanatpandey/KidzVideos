import axios from "axios";
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const UploadVideo = () => {

    const[videoName, setVideoName] = useState('');
    const[description, setDescription] = useState('');
    const[ageGroup, setAgeGroup] = useState('');
    const[genere, setGenere] = useState('');
    const[file, setFile] = useState('');
    const[error, setError] = useState('');
    const[success, setSuccess] = useState('');

    const navigate = useNavigate();

    const handleUpload = async(e) => {
        e.preventDefault();
        setError('');
        setSuccess('');

        const formData = new FormData();
        formData.append('videoName', videoName);
        formData.append('description', description);
        formData.append('ageGroup', ageGroup);
        formData.append('genere', genere);
        formData.append('file', file);
        
        try{
            const token = localStorage.getItem('authToken');
            await axios.post('http://localhost/api/content/video/save', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    'Authorization': `Bearer ${token}`,
                }
            });
            setSuccess('Video Uploaded Successfully');
            navigate('/');
        }catch(err){
            console.error(err);
            setError('Upload Failed, Check input or permissions');
        }
    };

    return (
        <div className="min-h-screen flex justify-center items-center bg-gradient-to-br from-yellow-100 via-pink-100 to-purple-100">
            <form onSubmit={handleUpload} className="bg-white p-8 rounded-3xl shadow-3xl w-full max-w-lg border border-pink-300">
                <h2 className="text-2xl font-bold text-pink-500 text-center mb-4">Upload New Video</h2>

                <input 
                    type="text"
                    placeholder="Video Name"
                    value={videoName}
                    onChange={(e) => setVideoName(e.target.value)}
                    className="w-full p-2 border border-gray-300 rounded mt-2"
                    required
                />

                <textarea
                    placeholder="Description"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    rows={4}
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
                    value={genere}
                    onChange={(e) => setGenere(e.target.value)}
                    className="w-full p-2 border border-gray-300 rounded mt-3"
                    required
                />
                <input
                    type="file"
                    accept="video/*"
                    onChange={(e) => setFile(e.target.files[0])}
                    className="w-full mt-3"
                    required
                />
                {error && <p className="text-red-500 mt-2 text-sm">{error}</p>}
                {success && <p className="text-green-500 mt-2 text-sm">{success}</p>}

                <button
                    type="submit"
                    className="w-full mt-5 bg-pink-500 hover:bg-pink-600 text-white font-semibold py-2 rounded"
                >
                    Upload Video
                </button>
            </form>
        </div> 
    );
};

export default UploadVideo;