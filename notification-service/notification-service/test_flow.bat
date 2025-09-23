@echo off
echo 1. Check existing notifications...
curl -X GET http://localhost:8081/api/notifications/all

echo.
echo.
echo 2. Create a test notification...
curl -X POST http://localhost:8081/api/notifications/send -H "Content-Type: application/json" -d "{\"memberId\":1,\"message\":\"Test message\"}"

echo.
echo.
echo 3. Check notifications again...
curl -X GET http://localhost:8081/api/notifications/all

echo.
echo.
echo 4. Delete notification with ID 1...
curl -X DELETE http://localhost:8081/api/notifications/1