@echo off 
if not DEFINED IS_MINIMIZED set IS_MINIMIZED=1 && start "" /min "%~dpnx0" %* && exit

set mypath=%~dp0

%mypath%jre\\bin\\java --enable-preview -jar %mypath%TicTacToe.jar

exit
