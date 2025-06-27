# 🎥 Video Streaming App

A full-stack video streaming platform using **React (Vite)** for the frontend and **Spring Boot** for the backend.  
This app allows users to upload videos, which are processed into **HLS (.m3u8 + .ts)** segments using **FFmpeg**, and streamed with **autoplay and looping** via **HLS.js**.

---

## 🚀 Features

- 📤 Upload video files
- 🛠️ Convert videos to HLS format (master.m3u8 + segments)
- 📺 Play videos with HLS.js (adaptive streaming)
- 🔁 Autoplay + Loop first video on startup
- 🔢 Switch videos by entering a UUID
- 📦 Frontend built with React + Tailwind + Flowbite
- ☕ Backend built with Spring Boot + Hibernate (MySQL)

---

## 📁 Project Structure
video-streaming-app/
├── backend/ # Spring Boot app
├── frontend/ # React + Vite app


---

## 🖥️ How to Run the Application Locally

### ⚙️ Backend (Spring Boot)

> 📍 Pre-requisites:
> - Java 17+
> - Maven
> - FFmpeg installed & added to system path
> - MySQL running (adjust DB settings if needed)

1. Open terminal and go to the backend folder:
```bash
cd backend

2.Configure application.properties (DB + HSL storage path)

3.Run the app:
mvn spring-boot:run

💻 Frontend (React + Vite)
📍 Pre-requisites:
Node.js (v16 or higher)
npm or yarn

Go to the frontend folder:
cd frontend

Install dependencies:
npm install

Start the development server:
npm run dev
🚀 Frontend runs at: http://localhost:5173


📝 Usage
Upload a video using the VideoUpload form on the right.
The backend will convert the video into HLS format using FFmpeg.
The default video plays automatically.
Enter any valid video ID in the input box to load and play a different video.

🔧 Configuration Tips
Ensure ffmpeg is installed and accessible from terminal
Check backend’s application.properties:
file.video.hsl=videos_hsl/
spring.datasource.url=jdbc:mysql://localhost:3306/your_db

🧰 Tech Stack
Frontend: React, Vite, Tailwind CSS, Flowbite, HLS.js
Backend: Spring Boot, Hibernate, MySQL, FFmpeg
Video Streaming: HLS (.m3u8 + .ts), served over HTTP
Notifications: react-hot-toast


