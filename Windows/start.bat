@echo off 
if not DEFINED IS_MINIMIZED set IS_MINIMIZED=1 && start "" /min "%~dpnx0" %* && exit

jre\\bin\\java --enable-preview -jar TicTacToe.jar

exit