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
    *   The frontend will be accessible at `http://localhost:5173`.
    *   The backend API will be accessible at `http://localhost:8080`.

3.  **Stopping the application:**
    To stop the application, run:
    ```bash
    docker-compose down
    ```

## Project Structure

*   `profeco-front`: Contains the React frontend application.
*   `profeco-back`: Contains the Spring Boot backend application.
*   `compose.yaml`: Docker Compose file to orchestrate the services.
*   `.env`: Environment variables for Docker Compose (you need to create this).

## Development Workflow

### Backend Auto-Restart

When running the application with Docker Compose, the backend service (`profeco-back`) is configured for live reloading. If you make changes to the backend Java code and then run:

```bash
cd profeco-back
mvn clean compile
```

The Docker container for the backend will automatically detect these changes, stop the currently running Spring Boot application, and restart it with the new compiled code. This allows for a faster development cycle without needing to manually rebuild or restart the Docker container.
