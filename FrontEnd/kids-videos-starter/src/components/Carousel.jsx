import React from 'react';
import VideoCard from './VideoCard';

const Carousel = ({ category }) => {
  return (
    <div className="mb-6">
      <h3 className="text-2xl font-semibold mb-3 text-pink-600">{category} Videos</h3>
      <div className="carousel-container">
        {[1,2,3,4].map(id => (
          <VideoCard key={id} id={id} title={`${category} Video ${id}`} />
        ))}
      </div>
    </div>
  );
};

export default Carousel;