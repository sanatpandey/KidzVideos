import React from 'react';
import { Link } from 'react-router-dom';

const VideoCard = ({ id, title }) => {
  return (
    <Link to={`/detail/${id}`} className="card min-w-[200px] hover:scale-105 transition-transform duration-200">
      <img src={`https://via.placeholder.com/200x120?text=${title}`} alt={title} className="w-full h-32 object-cover rounded-t-xl" />
      <div className="p-3 bg-pink-50 rounded-b-xl">
        <p className="text-sm font-medium text-pink-800 text-center">{title}</p>
      </div>
    </Link>
  );
};

export default VideoCard;