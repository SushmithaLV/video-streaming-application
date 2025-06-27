import React, { useEffect, useRef } from "react";
import Hls from "hls.js";
import toast from "react-hot-toast";

function VideoPlayer({ src }) {
  const videoRef = useRef(null);

  useEffect(() => {
    let hls;

    if (!src) return;

    const video = videoRef.current;

    if (Hls.isSupported()) {
      hls = new Hls();
      hls.loadSource(src);
      hls.attachMedia(video);

      hls.on(Hls.Events.MANIFEST_PARSED, function () {
        video
          .play()
          .then(() => {
            console.log("Autoplay started");
          })
          .catch(err => {
            console.warn("Autoplay blocked:", err);
          });
      });

      hls.on(Hls.Events.ERROR, function (event, data) {
        console.error("HLS.js error:", data);
        toast.error("HLS Playback Error");
      });

    } else if (video.canPlayType("application/vnd.apple.mpegurl")) {
      // For Safari
      video.src = src;
      video.addEventListener("loadedmetadata", () => {
        video
          .play()
          .then(() => {
            console.log("Autoplay started");
          })
          .catch(err => {
            console.warn("Autoplay blocked:", err);
          });
      });
    } else {
      toast.error("Your browser does not support HLS playback");
    }

    return () => {
      if (hls) {
        hls.destroy();
      }
    };
  }, [src]);

  return (
    <div className="w-full">
      <video
        ref={videoRef}
        className="rounded shadow"
        controls
        muted // ✅ required for autoplay
        autoPlay // ✅ optional but recommended
        loop
        style={{ width: "100%", height: "500px" }}
      ></video>
    </div>
  );
}

export default VideoPlayer;
