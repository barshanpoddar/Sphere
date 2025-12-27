@echo off
echo Checking Kotlin code formatting...
echo.

REM Run custom check task
call .\gradlew :app:checkCodeFormat

echo.
echo Note: Composable function names starting with uppercase letters
echo are flagged as violations but this is correct for Jetpack Compose.
echo Only indentation and spacing issues are considered formatting errors.
echo.
pause