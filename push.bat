@echo off
setlocal enabledelayedexpansion
for /f "tokens=2 delims==" %%a in ('findstr "mod_version" gradle.properties') do (
    set VERSION=%%a
)
set VERSION=!VERSION: =!
if "%~1"=="" (
    echo [Error] Please provide a commit message! Format: .\push.bat "message"
    exit /b 1
)
set MSG=%~1
set COMMIT_MSG=!VERSION!:!MSG!
echo [Pushing] Commit Message: !COMMIT_MSG!
git add .
git commit -m "!COMMIT_MSG!"
git push
echo [Done] Pushed version !VERSION!