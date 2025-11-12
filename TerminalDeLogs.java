import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TerminalDeLogs {
    private static final int PORTA_LOG = 7000;
    
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORTA_LOG);
            System.out.println("===================================================");
            System.out.println("        TERMINAL DE LOGS DO ROTEADOR");
            System.out.println("        Aguardando conexão...");
            System.out.println("===================================================\n");
            
            Socket socket = serverSocket.accept();
            System.out.println("Roteador conectado. Exibindo logs:\n");
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String linha;
            
            while ((linha = reader.readLine()) != null) {
                System.out.println(linha);
            }
            
            reader.close();
            socket.close();
            serverSocket.close();
        } catch (Exception e) {
            System.err.println("Erro no terminal de logs: " + e.getMessage());
        }
    }
}
