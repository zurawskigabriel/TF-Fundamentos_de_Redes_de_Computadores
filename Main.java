import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import Entidades.GerenciadorDeOutput;
import Entidades.Roteador;

public class Main
{
    public static void main(String[] args)
    {
        // Abre o terminal de logs em uma nova janela
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                // Windows
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "start", "cmd", "/k", 
                    "java -cp . TerminalDeLogs");
                pb.directory(new java.io.File(System.getProperty("user.dir")));
                pb.start();
            } else {
                // Linux/Mac
                System.err.println("Sistema operacional não suportado para abertura automática de terminais.");
                System.err.println("Execute manualmente: java TerminalDeLogs");
            }
            
            // Aguarda um pouco para o terminal de logs iniciar
            Thread.sleep(2000);
        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao abrir terminal de logs: " + e.getMessage());
            System.err.println("Execute manualmente: java TerminalDeLogs");
        }

        String ip = "";
        try
        {
            InetAddress enderecoLocal = InetAddress.getLocalHost();
            ip = enderecoLocal.getHostAddress();
        }
        catch (UnknownHostException e)
        {
            System.err.println("Erro ao obter IP: " + e.getMessage() + ". Fechando aplicação.");
            System.exit(1);
        }

        // Inicializa o gerenciador de output
        GerenciadorDeOutput.inicializar();

        Roteador roteador = new Roteador(ip);
        roteador.Ligar();
    }
}