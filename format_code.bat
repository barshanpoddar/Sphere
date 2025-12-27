@echo off
echo Formatting Kotlin code like Flutter...
echo.

REM Run custom format task
call .\gradlew :app:formatCode

echo.
echo Code formatting completed! (Some warnings about Composable function names are expected)
echo.
pause