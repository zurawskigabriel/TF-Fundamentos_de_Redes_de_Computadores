package Threads;
import java.net.DatagramPacket;

import Entidades.Roteador;

public class ThreadDeRecepcao implements Runnable
{
    private static final int TAMANHO_DO_BUFFER = 1024;

    private byte[] buffer;
    private Roteador roteador;

    public ThreadDeRecepcao(Roteador roteador)
    {
        this.buffer = new byte[TAMANHO_DO_BUFFER];
        this.roteador = roteador;
    }

    @Override
    public void run()
    {
        try
        {
            while (!roteador.socket.isClosed())
            {
                DatagramPacket pacoteRecebido = new DatagramPacket(buffer, buffer.length);

                roteador.socket.receive(pacoteRecebido);

                String mensagem = new String(pacoteRecebido.getData(), 0, pacoteRecebido.getLength());
                String ipOrigem = pacoteRecebido.getAddress().getHostAddress();
                int portaOrigem = pacoteRecebido.getPort();

                char tipo = mensagem.charAt(0);

                switch (tipo)
                {
                    case '@':
                        TratarMensagemDeAnuncio(mensagem, ipOrigem, portaOrigem);
                        break;
                    case '*':
                        TratarMensagemDeAtualizacaoDeTabela(mensagem, ipOrigem, portaOrigem);
                        break;
                    case '!':
                        TratarMensagemDeTexto(mensagem, ipOrigem, portaOrigem);
                        break;
                    default:
                        System.err.println("\nTipo desconhecido de mensagem de " + ipOrigem + ":" + portaOrigem + " | Mensagem: " + mensagem);
                        break;
                }

                System.out.print("> ");
            }
        }
        catch (Exception e)
        {
            if (!roteador.socket.isClosed())
            {
                System.err.println("Erro ao receber mensagem: " + e.getMessage());
            }
        }
    }

    private void TratarMensagemDeAnuncio(String mensagem, String ipOrigem, int portaOrigem)
    {
        String ipAnunciado = mensagem.substring(1);
        System.out.println("\n[RECEBIDO @] | (" + ipOrigem + ":" + portaOrigem + ") | IP Anunciado: " + ipAnunciado + " [" + mensagem +']');
        // TODO: Implementar o processamento do anúncio de vizinho
    }

    private void TratarMensagemDeAtualizacaoDeTabela(String mensagem, String ipOrigem, int portaOrigem)
    {
        System.out.println("\n[RECEBIDO *] | (" + ipOrigem + ":" + portaOrigem + ") | [" + mensagem +']');
        // TODO: Implementar o processamento da atualização da tabela de roteamento
    }

    private void TratarMensagemDeTexto(String mensagem, String ipOrigem, int portaOrigem)
    {
        try
        {
            String mensagemSemTipo = mensagem.substring(1);
            String ipOrigemDoCorpo = mensagemSemTipo.split(";")[0];
            String ipDestinoDoCorpo = mensagemSemTipo.split(";")[1];
            String texto = mensagemSemTipo.split(";")[2];

            if (ipDestinoDoCorpo == roteador.ip)
            {
                System.out.println("\n[RECEBIDO ! ENTREGA REALIZADA] | (" + ipOrigem + ":" + portaOrigem + ") | Texto: " + texto + " Origem: " + ipOrigemDoCorpo + " Destino: " + ipDestinoDoCorpo + "| [" + mensagem +']');
                return;
            }
            else
            {
                System.out.println("\n[RECEBIDO ! ENTREGA NÃO REALIZADA] | (" + ipOrigem + ":" + portaOrigem + ") | Texto: " + texto + " Origem: " + ipOrigemDoCorpo + " Destino: " + ipDestinoDoCorpo + "| [" + mensagem +']');
                // TODO: Implementar o reencaminhamento da mensagem para o próximo salto
            }
        }
        catch (Exception e)
        {
            System.out.println("Erro ao processar mensagem de texto: " + e.getMessage());
        }
    }
}