import React from 'react';
import { useParams } from 'react-router-dom';

const Detail = () => {
  const { id } = useParams();
  return (
    <div className="p-4">
      <h2 className="text-2xl font-bold">Video Detail: {id}</h2>
      <video controls className="w-full mt-4 rounded-xl">
        <source src={`https://example.com/videos/${id}.mp4`} type="video/mp4" />
      </video>
    </div>
  );
};

export default Detail;