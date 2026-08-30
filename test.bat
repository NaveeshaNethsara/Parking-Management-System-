@echo off
title Parking Management System - Test Suite
echo ===================================================
echo   Running Automated Test Suite
echo ===================================================
if not exist "bin" mkdir bin
javac -encoding UTF-8 -d bin src/models/*.java src/utils/*.java src/data/*.java src/services/*.java src/gui/*.java src/SystemTest.java
if %errorlevel% neq 0 (
    echo Compilation failed.
    pause
    exit /b %errorlevel%
)
java -ea -cp bin SystemTest
pause
exit /b 0
