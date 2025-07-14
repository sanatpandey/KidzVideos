import axios from "axios";
import React, { useEffect, useRef, useState } from "react";
import { useParams, useSearchParams } from "react-router-dom";

const Watch = () => {
    const {id} = useParams();
    const [searchParams] = useSearchParams();
    const startTime = parseFloat(searchParams.get("t")) || 0;
    const[videoUrl, setVideoUrl] = useState("");
    const videoRef = useRef();

    useEffect(() => {
        if (id) {
          setVideoUrl(`http://localhost:8082/video/stream/${id}`);
        }
      }, [id]);

    useEffect(()=> {
        const interval = setInterval(() => {
            if(videoRef.current){
             const currentTime = videoRef.current.currentTime;
             sendProgress(currentTime);
            }
        }, 10000);

        return ()=> clearInterval(interval);
    }, []);

    const sendProgress = async (currentTime) => {
        try{
            const token = localStorage.getItem("authToken");
            await axios.post(`http://localhost:8082/video/${id}/progress`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
        }catch(error){
            console.error("Progress save failed");
        }
    };

    return (
        <div className="flex justify-center items-center h-screen bg-black">
            {videoUrl ? (
                <video 
                    ref={videoRef}
                    controls
                    autoPlay
                    className="w-full max-w-4xl rounded shadow-xl"
                    onLoadedMetadata={()=> {
                        videoRef.curret.currentTime = startTime;
                    }}
                >
                    <source src={videoUrl} type="video/mp4" />
                </video>
            ) : (
                <div className="text-white text-xl">Loading.....</div>
            )}
        </div>
    );
};

export default Watch;