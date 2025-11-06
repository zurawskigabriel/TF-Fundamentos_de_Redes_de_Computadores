import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class RecebedorMensagens implements Runnable {
    private DatagramSocket socket;
    private static final int BUFFER_SIZE = 1024;

    public RecebedorMensagens(DatagramSocket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[BUFFER_SIZE];

        try {
            while (!socket.isClosed()) {
                // Cria um pacote para receber dados
                DatagramPacket pacoteRecebido = new DatagramPacket(buffer, buffer.length);

                // Bloqueia até receber um pacote
                socket.receive(pacoteRecebido);

                // Extrai informações do pacote
                String mensagem = new String(
                    pacoteRecebido.getData(),
                    0,
                    pacoteRecebido.getLength()
                );
                String ipOrigem = pacoteRecebido.getAddress().getHostAddress();
                int portaOrigem = pacoteRecebido.getPort();

                // Exibe a mensagem recebida
                System.out.println("\n[RECEBIDO] De " + ipOrigem + ":" + portaOrigem);
                System.out.println("  Mensagem: " + mensagem);
                System.out.print("> "); // Reimprime o prompt
            }
        } catch (Exception e) {
            if (!socket.isClosed()) {
                System.err.println("Erro ao receber mensagem: " + e.getMessage());
            }
        }
    }
}