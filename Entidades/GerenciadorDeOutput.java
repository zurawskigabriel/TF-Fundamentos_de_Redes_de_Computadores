package Entidades;

import java.io.PrintWriter;
import java.net.Socket;

public class GerenciadorDeOutput
{
    private static PrintWriter writerLog = null;
    private static Socket socketLog = null;
    private static final int PORTA_LOG = 7000;

    public static void Ativar()
    {
        try
        {
            // Conecta ao terminal de log
            socketLog = new Socket("localhost", PORTA_LOG);
            writerLog = new PrintWriter(socketLog.getOutputStream(), true);
            System.out.println("Conectado ao terminal de logs.");
        }
        catch (Exception e)
        {
            System.err.println("Erro ao conectar ao terminal de logs: " + e.getMessage());
        }
    }

    public static void Log(String mensagem)
    {
        if (writerLog != null)
        {
            writerLog.println(mensagem);
        }
        else
        {
            System.out.println(mensagem);
        }
    }

    public static void Desativar()
    {
        try
        {
            if (writerLog != null)
            {
                writerLog.close();
            }

            if (socketLog != null)
            {
                socketLog.close();
            }
        }
        catch (Exception e)
        {
            System.err.println("Erro ao fechar conexão de log: " + e.getMessage());
        }
    }
}
