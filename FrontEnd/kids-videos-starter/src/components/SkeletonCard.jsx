import React from 'react';

const SkeletonCard = () => {
  return (
    <div className="w-64 bg-white rounded-lg shadow-md animate-pulse overflow-hidden">
      <div className="h-40 bg-gray-300 w-full" />
      <div className="p-3 space-y-2">
        <div className="h-4 bg-gray-300 rounded w-3/4" />
        <div className="h-3 bg-gray-200 rounded w-1/2" />
      </div>
    </div>
  );
};

export default SkeletonCard;
