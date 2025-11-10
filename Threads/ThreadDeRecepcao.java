package Threads;
import java.net.DatagramPacket;
import java.util.List;

import Entidades.Rota;
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
                        TratarMensagemDeAnuncio(pacoteRecebido);
                        break;
                    case '*':
                        TratarMensagemDeAtualizacaoDeTabela(pacoteRecebido);
                        break;
                    case '!':
                        TratarMensagemDeTexto(pacoteRecebido);
                        break;
                    default:
                        System.err.println("\n[ERRO] Tipo desconhecido de mensagem de " + ipOrigem + ":" + portaOrigem + " | Corpo da mensagem: " + mensagem);
                        break;
                }

                System.out.print("> ");
            }
        }
        catch (Exception e)
        {
            if (!roteador.socket.isClosed())
            {
                System.err.println("[ERRO] Erro ao receber mensagem: " + e.getMessage());
            }
        }
    }

    private void TratarMensagemDeAnuncio(DatagramPacket pacoteRecebido)
    {
        String mensagem = new String(pacoteRecebido.getData(), 0, pacoteRecebido.getLength());

        String ipAnunciado = mensagem.substring(1);
        System.out.println("\n[RECEBIDO @] | IP Anunciado: " + ipAnunciado + " [" + mensagem +']');
        roteador.tabelaDeVizinhos.AdicionarVizinho(ipAnunciado);
        roteador.tabelaDeRoteamento.AdicionarRota(ipAnunciado, 1, ipAnunciado); // Não sei se está certo colocar o próximo salto como o próprio IP anunciado, perguntar para professora.
    }

    private void TratarMensagemDeAtualizacaoDeTabela(DatagramPacket pacoteRecebido)
    {
        List<Rota> novasRotas;

        String mensagem = new String(pacoteRecebido.getData(), 0, pacoteRecebido.getLength());

        System.out.println("\n[RECEBIDO *] | [" + mensagem +']');

        // TODO: Implementar o parser das mensagens com as novas rotas (conforme o enunciado) e atualizar a tabela de roteamento

        //roteador.AtualizarTabela(mensagem);
    }

    private void TratarMensagemDeTexto(DatagramPacket pacoteRecebido)
    {
        try
        {
            String mensagem = new String(pacoteRecebido.getData(), 0, pacoteRecebido.getLength());

            String mensagemSemTipo = mensagem.substring(1);
            String ipOrigemDoCorpo = mensagemSemTipo.split(";")[0];
            String ipDestinoDoCorpo = mensagemSemTipo.split(";")[1];
            String texto = mensagemSemTipo.split(";")[2];

            if (ipDestinoDoCorpo == roteador.ip)
            {
                System.out.println("\n[RECEBIDO ! ENTREGA REALIZADA] | Texto: " + texto + " Origem: " + ipOrigemDoCorpo + " Destino: " + ipDestinoDoCorpo + "| [" + mensagem +']');
                return;
            }
            else
            {
                System.out.println("\n[RECEBIDO ! ENTREGA NÃO REALIZADA] | Texto: " + texto + " Origem: " + ipOrigemDoCorpo + " Destino: " + ipDestinoDoCorpo + "| [" + mensagem +']');
                roteador.EnviarMensagemDeTexto(ipDestinoDoCorpo, mensagem);
            }
        }
        catch (Exception e)
        {
            System.out.println("[ERRO] Erro ao processar mensagem de texto: " + e.getMessage());
        }
    }
}