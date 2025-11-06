import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Roteador
{
    private static final int PORTA_LOCAL = 5000;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args)
    {
        try
        {
            DatagramSocket socket = new DatagramSocket(PORTA_LOCAL);
            System.out.println("===================================================");
            System.out.println("Roteador iniciado na porta " + PORTA_LOCAL);
            System.out.println("Comandos disponíveis:");
            System.out.println("  enviar <ip> <porta> <mensagem>");
            System.out.println("  sair");
            System.out.println("===================================================");

            Thread threadRecepcao = new Thread(new RecebedorMensagens(socket));
            Thread threadEnvio    = new Thread(new Terminal(socket));
            threadRecepcao.start();
            threadEnvio.start();

            threadEnvio.join();

            socket.close();
            System.out.println("Roteador encerrado.");
            System.exit(0);

        } catch (Exception e) {
            System.err.println("Erro ao iniciar o roteador: " + e.getMessage());
            e.printStackTrace();
        }
    }
}