import React from 'react';
import VideoCard from './VideoCard';
import SkeletonCard from './SkeletonCard';

const Carousel = ({ category, videos = [], loading = false }) => {
  const renderContent = () => {
    if(loading){
      return Array(4)
        .fill(0)
        .map((_, i) => <SkeletonCard key={i} />);
    }
    return videos.map((video) => (
      <VideoCard key={video.id} video={video} />
    ));
  };

  if (!loading &&(!videos || videos.length === 0)) return null;

  return (
    <div className="mb-6">
      <h3 className="text-2xl font-semibold mb-3 text-pink-600">{category} Videos</h3>
      <div className="carousel-container flex gap-4 overflow-x-auto">
        {renderContent()}
      </div>
    </div>
  );
};

export default Carousel;
