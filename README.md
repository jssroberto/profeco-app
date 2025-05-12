# Profeco App

This application allows users to compare prices of products across different supermarkets and report inconsistencies.

## Running with Docker

To run this application using Docker, ensure you have Docker and Docker Compose installed on your system.

1.  **Run the application:**
    Use Docker Compose to build and run the services defined in the `compose.yaml` file:

    ```bash
    docker-compose up -d --build
    ```

    The `-d` flag runs the containers in detached mode. The `--build` flag forces a rebuild of the images.

2.  **Access the application:**

    - The frontend will be accessible at `http://localhost:5173`.
    - The backend API will be accessible at `http://localhost:8080`.

3.  **Stopping the application:**
    To stop the application, run:
    ```bash
    docker-compose down
    ```

## Project Structure

- `profeco-front`: Contains the React frontend application.
- `profeco-back`: Contains the Spring Boot backend application.
- `compose.yaml`: Docker Compose file to orchestrate the services.
- `.env`: Environment variables for Docker Compose (you need to create this).

## Development Workflow

This project is configured for a streamlined development experience using Docker, with a focus on rapid feedback and iteration. Both the frontend and backend services leverage live reloading and auto-restart capabilities within their respective Docker containers.

### Frontend: Vite with Hot Module Replacement (HMR)

The `profeco-front` service, built with React and Vite, utilizes Hot Module Replacement (HMR). When you make changes to the frontend code (e.g., `.tsx`, `.css` files):

- Vite automatically updates the relevant modules in the browser without a full page reload.
- This provides instant feedback on your changes, significantly speeding up UI development.
- The `Dockerfile.dev` for the frontend is optimized for this development workflow.

### Backend: Spring Boot DevTools Auto-Restart

The `profeco-back` service, a Spring Boot application, is configured with Spring Boot DevTools for an efficient development workflow when using Docker Compose.

- When you modify and save your Java source code, the running Spring Boot application (started via `mvn spring-boot:run` with DevTools enabled as per `profeco-back/Dockerfile.dev`) automatically detects these changes.
- This triggers a quick restart of the application within its Docker container, incorporating your latest modifications.
