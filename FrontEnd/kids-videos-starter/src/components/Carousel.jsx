import React from 'react';
import VideoCard from './VideoCard';

const Carousel = ({ category, videos }) => {
  if (!videos || videos.length === 0) return null;

  return (
    <div className="mb-6">
      <h3 className="text-2xl font-semibold mb-3 text-pink-600">{category} Videos</h3>
      <div className="carousel-container flex gap-4 overflow-x-auto">
        {videos.map((video) => (
          <VideoCard key={video.id} video={video} />
        ))}
      </div>
    </div>
  );
};

export default Carousel;
