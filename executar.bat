@echo off
echo ===================================================
echo   ROTEADOR - Sistema de Roteamento Distribuido
echo ===================================================
echo.
echo Compilando o projeto...
javac -encoding UTF-8 Main.java TerminalDeLogs.java Entidades/*.java Threads/*.java

if errorlevel 1 (
    echo.
    echo [ERRO] Falha na compilacao!
    pause
    exit /b 1
)

echo.
echo Compilacao concluida com sucesso!
echo.
echo Iniciando o roteador...
echo.

java Main

echo.
echo Aplicacao encerrada.
pause
