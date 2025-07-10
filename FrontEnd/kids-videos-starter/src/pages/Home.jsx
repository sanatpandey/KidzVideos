import React, { useEffect, useState } from 'react';
import Carousel from '../components/Carousel';
import axios from 'axios';

const Home = () => {
  const[videos, setVideos] = useState([]);

  useEffect(() => {
    const fetchVideos = async() => {
      try{
        const response = await axios.get('http://localhost:8082/video/all');

        setVideos(response.data);
      }catch(error){
        console.error("Error fetching videos", error)
      }
    };
    fetchVideos();
  }, []);

  const groupByGenre = (genre) => {
    return videos.filter(video => video.genre === genre)
  };

  if (videos.length === 0) {
    return <div className="text-center mt-20 text-pink-500">Loading videos...</div>;
  }

  return (
    <div className="p-6 bg-gradient-to-br from-pink-50 via-yellow-50 to-purple-50 min-h-screen">
      <h1 className="text-4xl font-extrabold mb-8 text-center text-pink-700 drop-shadow-md">Welcome to KidsFlix 🎉</h1>
      <Carousel category="New" videos= {groupByGenre('New Release')}/>
      <Carousel category="Cartoon" videos= {groupByGenre('Cartoon')}/>
      <Carousel category="Education" videos= {groupByGenre('Education')}/>
    </div>
  );
};

export default Home;