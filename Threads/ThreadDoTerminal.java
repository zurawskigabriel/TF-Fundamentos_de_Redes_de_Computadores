package Threads;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

import Entidades.Roteador;

public class ThreadDoTerminal implements Runnable
{
    private Scanner scanner;
    private Roteador roteador;

    public ThreadDoTerminal(Roteador roteador)
    {
        this.scanner = new Scanner(System.in);
        this.roteador = roteador;
    }

    @Override
    public void run()
    {
        boolean continuar = true;
        try
        {
            while (continuar)
            {
                System.out.print("> ");
                String comando = scanner.nextLine().trim();
                String tipoDeComando = comando.split(" ")[0];

                if (comando.isEmpty())
                {
                    continue;
                }

                switch (tipoDeComando)
                {
                    case "sair":
                        System.out.println("Saindo...");
                        continuar = false;
                        break;
                    case "enviar":
                        String[] partes = comando.substring(7).split(" ", 2);

                        if (partes.length < 2)
                        {
                            System.out.println("[ERRO] Formato incorreto. Use: enviar <ip> <mensagem>");
                            return;
                        }

                        String ipDestino = partes[0];
                        String mensagem = partes[1];

                        roteador.EnviarMensagemDeTexto(ipDestino, mensagem);
                        break;
                    default:
                        System.out.println("[ERRO] Comando não reconhecido.");
                        break;
                }
            }
        }
        catch (Exception e)
        {
            System.err.println("[ERRO] Erro no terminal: " + e.getMessage());
        }
        finally
        {
            scanner.close();
        }
    }
}
