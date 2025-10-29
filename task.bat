@echo off
rem Run from the folder that contains pom.xml
rem this batch file is for shortcut
rem pushd %~dp0
rem mvn exec:java "-Dexec.mainClass=com.example.todo.Main" "-Dexec.args=%*"
mvn -q exec:java "-Dexec.mainClass=com.example.todo.Main" "-Dexec.args=%*"

