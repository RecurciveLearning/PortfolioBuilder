@echo off
echo Building Docker image...
docker build -t portfolio:latest .

echo.
echo Starting containers...
docker-compose up -d

echo.
echo Waiting for services to start...
timeout /t 30

echo.
echo Checking container status...
docker-compose ps

echo.
echo Application should be running at http://localhost:8080
echo MySQL: localhost:3306
echo Redis: localhost:6379
echo.
echo To view logs: docker-compose logs -f app
echo To stop: docker-compose down
