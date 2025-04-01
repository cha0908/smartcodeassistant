# Smart Code Assistant

A modern code assistant powered by local LLMs via Ollama.

## Project Overview

Smart Code Assistant is designed to help developers with coding tasks using AI. It features:

- Frontend with a sleek UI for prompt input and response display
- Spring Boot backend that communicates directly with the Ollama API
- Code generation, analysis, and more via qwen2.5-coder:0.5b LLM

## Project Structure

- `frontend/` - React TypeScript application
- `backend/` - Spring Boot Java application

## Prerequisites

- Node.js (v14+) and npm for frontend
- Java 17+ and Maven for backend
- [Ollama](https://ollama.ai/) installed locally
- qwen2.5-coder:0.5b model pulled in Ollama

## Quick Start

### Setting up Ollama

1. Install Ollama from [ollama.ai](https://ollama.ai/)
2. Start Ollama:
   ```
   ollama serve
   ```
3. Pull the qwen2.5-coder model:
   ```
   ollama pull qwen2.5-coder:0.5b
   ```

### Running the Backend

1. Navigate to the backend directory:
   ```
   cd backend
   ```
2. Build the project:
   ```
   mvn clean install
   ```
3. Run the application:
   ```
   mvn spring-boot:run
   ```
   The backend will be available at http://localhost:8080

### Running the Frontend

1. Navigate to the frontend directory:
   ```
   cd frontend
   ```
2. Install dependencies:
   ```
   npm install
   ```
3. Start the development server:
   ```
   npm start
   ```
   The frontend will be available at http://localhost:3000

## Features

- Multi-line code prompt input
- Syntax highlighting for both input and output code
- Copy output with one click
- Local LLM processing (no data sent to external APIs)

## Team

Developed by The Code Reapers team.
