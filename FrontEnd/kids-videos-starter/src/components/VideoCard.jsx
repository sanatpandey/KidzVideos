import React from 'react';

const VideoCard = ({ video }) => {
  return (
    <div className="w-64 bg-white rounded-lg shadow-md overflow-hidden">
      <img src={video.thumbnailUrl} alt={video.name} className="h-40 w-full object-cover" />
      <div className="p-3">
        <h4 className="font-bold text-lg">{video.name}</h4>
        <p className="text-sm text-gray-600">{video.genre}</p>
      </div>
    </div>
  );
};

export default VideoCard;
