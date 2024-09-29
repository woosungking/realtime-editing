import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import RabbitMQPage from "./rabbitMQ/RabbitMQPage.tsx";
import "./index.css";
import RedisPage from "./redis/RedisPage.tsx";
import MainPage from "./MainPage.tsx";
import RabbitMQRestPage from "./rabbitMQ/RabbitMQRestPage.tsx";
(window as any).global = window;

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/rabbitmq" element={<RabbitMQPage />} />
        <Route path="/rabbitmq/rest" element={<RabbitMQRestPage />} />
        <Route path="/redis" element={<RedisPage />} />
        <Route path="/" element={<MainPage />} />
      </Routes>
    </BrowserRouter>
  </StrictMode>
);
