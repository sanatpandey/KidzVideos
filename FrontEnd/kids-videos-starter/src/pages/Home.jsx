import React from 'react';
import Carousel from '../components/Carousel';

const Home = () => {
  return (
    <div className="p-6 bg-gradient-to-br from-pink-50 via-yellow-50 to-purple-50 min-h-screen">
      <h1 className="text-4xl font-extrabold mb-8 text-center text-pink-700 drop-shadow-md">Welcome to KidsFlix 🎉</h1>
      <Carousel category="New" />
      <Carousel category="Animals" />
      <Carousel category="Learning" />
    </div>
  );
};

export default Home;