@echo off
title Parking Management System
echo ===================================================
echo   Building and Launching Parking Management System
echo ===================================================
if not exist "bin" mkdir bin
javac -encoding UTF-8 -d bin src/models/*.java src/utils/*.java src/data/*.java src/services/*.java src/gui/*.java src/ParkingApp.java
if %errorlevel% neq 0 (
    echo Compilation failed.
    pause
    exit /b %errorlevel%
)
echo Starting application...
start javaw -cp bin ParkingApp
exit /b 0
