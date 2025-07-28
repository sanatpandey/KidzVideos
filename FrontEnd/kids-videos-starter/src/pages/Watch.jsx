import React, { useEffect, useRef, useState } from "react";
import { useParams, useSearchParams } from "react-router-dom";
import axiosSecured from '../utils/axiosSecured';

const Watch = () => {
  const { id: videoId } = useParams();
  const [searchParams] = useSearchParams();
  const [startTime, setStartTime] = useState(0);
  const [videoUrl, setVideoUrl] = useState("");
  const videoRef = useRef(null);

  // 📥 Get resume time
  useEffect(() => {
    const fetchProgress = async () => {
      try {
        const response = await axiosSecured.get("/video/progress/get", {
          params: { videoId },
        });
        const resumeTime = response.data;
        setStartTime(resumeTime || 0);
      } catch (error) {
        console.error("Failed to fetch resume time", error);
        setStartTime(0);
      }
    };

    if (videoId) {
      fetchProgress();
    }
  }, [videoId]);

  // 📺 Set video stream URL
  useEffect(() => {
    if (videoId) {
      setVideoUrl(`http://localhost:8082/video/stream/${videoId}`);
    }
  }, [videoId]);

  // 🕐 Periodic progress save every 10s
  useEffect(() => {
    const interval = setInterval(() => {
      if (videoRef.current) {
        const currentTime = videoRef.current.currentTime;
        sendProgress(currentTime);
      }
    }, 10000);

    return () => clearInterval(interval);
  }, [videoId]);

  // ⏸ Save progress on pause + interval
  useEffect(() => {
    const videoElement = videoRef.current;
    if (!videoElement) return;

    const handleProgressSave = () => {
      if (videoElement && videoElement.currentTime > 0) {
        sendProgress(videoElement.currentTime);
      }
    };

    videoElement.addEventListener("pause", handleProgressSave);
    const interval = setInterval(handleProgressSave, 10000);

    return () => {
      videoElement.removeEventListener("pause", handleProgressSave);
      clearInterval(interval);
    };
  }, [videoId]);

  // ✅ Send progress using secured axios
  const sendProgress = async (currentTime) => {
    try {
      await axiosSecured.post("/video/progress/save", null, {
        params: {
          videoId: videoId,
          seconds: Math.floor(currentTime),
        },
      });
    } catch (error) {
      console.error("Progress save failed", error);
    }
  };

  return (
    <div className="flex justify-center items-center h-screen bg-black">
      {videoUrl ? (
        <video
          ref={videoRef}
          controls
          autoPlay
          muted
          className="w-full max-w-4xl rounded shadow-xl"
          onLoadedMetadata={() => {
            const video = videoRef.current;
            if (!video) return;

            setTimeout(() => {
              video.currentTime = startTime;

              const playPromise = video.play();
              if (playPromise !== undefined) {
                playPromise
                  .then(() => console.log("Playback started"))
                  .catch((e) => console.warn("Autoplay failed:", e));
              }
            }, 200);
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
