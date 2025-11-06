import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Terminal implements Runnable
{
    private DatagramSocket socket;
    private Scanner scanner;

    public Terminal(DatagramSocket socket)
    {
        this.socket = socket;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run() {
        try {
            while (true) {
                System.out.print("> ");
                String comando = scanner.nextLine().trim();

                if (comando.isEmpty()) {
                    continue;
                }

                // Verifica comando de saída
                if (comando.equalsIgnoreCase("sair")) {
                    break;
                }

                // Processa comando de envio
                if (comando.toLowerCase().startsWith("enviar ")) {
                    processarEnvio(comando);
                } else {
                    System.out.println("Comando inválido. Use: enviar <ip> <porta> <mensagem>");
                }
            }
        } catch (Exception e) {
            System.err.println("Erro no enviador: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private void processarEnvio(String comando) {
        try {
            // Remove "enviar " do início
            String[] partes = comando.substring(7).split(" ", 3);

            if (partes.length < 3) {
                System.out.println("Formato incorreto. Use: enviar <ip> <porta> <mensagem>");
                return;
            }

            String ipDestino = partes[0];
            int portaDestino = Integer.parseInt(partes[1]);
            String mensagem = partes[2];

            // Converte a mensagem em bytes
            byte[] dados = mensagem.getBytes();

            // Cria o pacote UDP
            InetAddress enderecoDestino = InetAddress.getByName(ipDestino);
            DatagramPacket pacote = new DatagramPacket(
                dados,
                dados.length,
                enderecoDestino,
                portaDestino
            );

            // Envia o pacote
            socket.send(pacote);
            System.out.println("[ENVIADO] Para " + ipDestino + ":" + portaDestino);

        } catch (NumberFormatException e) {
            System.out.println("Porta inválida. Deve ser um número.");
        } catch (Exception e) {
            System.err.println("Erro ao enviar mensagem: " + e.getMessage());
        }
    }
}
