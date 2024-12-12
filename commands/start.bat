@echo off
start /min /b javaw -jar profile-integration-1.0.0-SNAPSHOT.jar
for /f "tokens=* delims=" %%i in ('wmic process where "commandline like '%%profile-integration%%'" get processid ^| findstr [0-9] ^| findstr /r /v "^$"') do (
    echo %%i> pid.txt
    goto :eof
)