@echo off
echo ===================================================
echo   ROTEADOR - Sistema de Roteamento Distribuido
echo ===================================================
echo.
echo Compilando o projeto...

REM Cria a pasta bin se não existir
if not exist "bin" mkdir bin

REM Compila todos os arquivos .java e coloca os .class na pasta bin
javac -encoding UTF-8 -d bin Main.java TerminalDeLogs.java Entidades/*.java Threads/*.java

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
echo Heartbeat: %1
echo.

REM Executa a partir da pasta bin
java -cp bin Main %1

echo.
echo Aplicacao encerrada.
pause