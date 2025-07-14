import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';

const Detail = () => {
  const location = useLocation();
  const video = location.state?.video
  const { id } = useParams();
  const[resumeTime, setResumeTime] = useState(0);
  const navigate = useNavigate();

  const handlePlay = (fromTime) => {
    navigate(`/watch/${id}?t=${fromTime}`);
  };

  if(!video) return <div className="text-center mt-10">Loading....</div>;

  return (
    <div className="p-6 max-w-4xl mx-auto bg-white shadow-xl rounded-xl">
      <img src={video.thumbnailUrl} alt="thumbnail" className="w-full h-64 object-cover rounded-md"/>
      <h2 className="text-3xl font-bold text-pink-600 mt-4">{video.name}</h2>
      <p className="text-gray-700 mt-2">{video.description}</p>
      <div className="mt-4 space-x-4">
        <button
          className="px-4 py-2 bg-pink-500 text-white rounded"
          onClick={() => handlePlay(0)}
        >
          Play from Start
        </button>
        <button 
          className="px-4 py-2 bg-yellow-500 text-white rounded"
          onClick={() => handlePlay(resumeTime)}
        >
          Resume {resumeTime > 0? `at ${Math.floor(resumeTime)}s`: ""}
        </button>
      </div>
    </div>
  );
};

export default Detail;