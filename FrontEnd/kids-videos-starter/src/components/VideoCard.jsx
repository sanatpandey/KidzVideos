// components/VideoCard.jsx
import React from 'react';
import { useNavigate } from 'react-router-dom';

const VideoCard = ({ video }) => {
  const navigate = useNavigate();

  const handleClick = () => {
    console.log("Navigating to video:", video.id);
    navigate(`/detail/${video.id}`, { state: {video}});
  };

  return (
    <div
      onClick={handleClick}
      className="cursor-pointer w-48 flex-shrink-0 hover:scale-105 transition duration-200 ease-in-out"
    >
      <img
        src={video.thumbnailUrl}
        alt={video.name}
        className="w-full h-28 object-cover rounded-md shadow"
      />
      <p className="mt-2 text-sm font-semibold text-center text-gray-800">{video.name}</p>
    </div>
  );
};

export default VideoCard;
