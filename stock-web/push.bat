@echo off
SET IMAGE_NAME=stock-web-image
SET CONTAINER_NAME=stock-web-container

echo Stopping and removing existing container if it exists...
docker ps -a --format "{{.Names}}" | findstr /I "%CONTAINER_NAME%" >nul
IF %ERRORLEVEL% == 0 (
    docker stop %CONTAINER_NAME%
    docker rm %CONTAINER_NAME%
)

echo Building Docker image...
docker build -t %IMAGE_NAME% docker/Dockerfile .

echo Running new container...
docker run -d --name %CONTAINER_NAME% -p 50001:8080 %IMAGE_NAME%

echo Container started successfully!
docker ps -a | findstr /I "%CONTAINER_NAME%"