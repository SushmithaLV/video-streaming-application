package com.stream.app.services.impl;

import com.stream.app.entities.Video;
import com.stream.app.repositories.VideoRepository;
import com.stream.app.services.VideoService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    @Value("${files.video}")
    String DIR;

    @Value("${file.video.hsl}")
    String HSL_DIR;


    private VideoRepository videoRepository;


    public VideoServiceImpl(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @PostConstruct
    public void init() {

        File file = new File(DIR);


        try {
            Files.createDirectories(Paths.get(HSL_DIR));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!file.exists()) {
            file.mkdir();
            System.out.println("Folder Created:");
        } else {
            System.out.println("Folder already created");
        }
        System.out.println("HLS_DIR used: " + HSL_DIR);

    }

    @Override
    public Video save(Video video, MultipartFile file) {
        // original file name

        try {


            String filename = file.getOriginalFilename();
            String contentType = file.getContentType();
            InputStream inputStream = file.getInputStream();


            // file path
            String cleanFileName = StringUtils.cleanPath(filename);


            //folder path : create

            String cleanFolder = StringUtils.cleanPath(DIR);


            // folder path with  filename
            Path path = Paths.get(cleanFolder, cleanFileName);

            System.out.println(contentType);
            System.out.println(path);

            // copy file to the folder
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);


            // video meta data

            video.setContentType(contentType);
            video.setFilePath(path.toString());
            Video savedVideo = videoRepository.save(video);
            //processing video
            processVideo(savedVideo.getVideoId());

            //delete actual video file and database entry  if exception

            // metadata save
            return savedVideo;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in processing video ");
        }


    }


    @Override
    public Video get(String videoId) {
        Video video = videoRepository.findById(videoId).orElseThrow(() -> new RuntimeException("video not found"));
        return video;
    }

    @Override
    public Video getByTitle(String title) {
        return null;
    }

    @Override
    public List<Video> getAll() {
        return videoRepository.findAll();
    }

    @Override
    public String processVideo(String videoId) {
        Video video = this.get(videoId);

        Path videoPath = Paths.get(video.getFilePath()).toAbsolutePath();
        Path outputPath = Paths.get(HSL_DIR, videoId).toAbsolutePath();

        try {
            Files.createDirectories(outputPath);

            String segmentPattern = outputPath.resolve("segment_%03d.ts").toString().replace("\\", "/");
            String masterFile = outputPath.resolve("master.m3u8").toString().replace("\\", "/");
            String inputFile = videoPath.toString().replace("\\", "/");

            List<String> command = List.of(
                "ffmpeg",
                "-i", inputFile,
                "-c:v", "libx264",
                "-c:a", "aac",
                "-strict", "-2",
                "-f", "hls",
                "-hls_time", "10",
                "-hls_list_size", "0",
                "-hls_segment_filename", segmentPattern,
                masterFile
            );

            System.out.println("FFmpeg command: " + String.join(" ", command));

            ProcessBuilder builder = new ProcessBuilder(command);
            builder.redirectErrorStream(true); // redirect stderr to stdout
            Process process = builder.start();

            // Read ffmpeg output
            try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[FFmpeg] " + line);
                }
            }

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException("FFmpeg processing failed with exit code " + exitCode);
            }

            return videoId;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Video processing failed", e);
        }
    }

}