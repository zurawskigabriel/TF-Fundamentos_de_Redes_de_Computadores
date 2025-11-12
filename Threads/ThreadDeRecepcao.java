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
        roteador.tabelaDeRoteamento.AdicionarRota(ipAnunciado, 1, ipAnunciado);
    }

    private void TratarMensagemDeAtualizacaoDeTabela(DatagramPacket pacoteRecebido)
    {
        List<Rota> novasRotas = new java.util.ArrayList<>();

        String mensagem = new String(pacoteRecebido.getData(), 0, pacoteRecebido.getLength());

        System.out.println("\n[RECEBIDO *] | [" + mensagem +']');

        try
        {
            // Obtém o IP do roteador que enviou a mensagem (próximo salto)
            String ipProximoSalto = pacoteRecebido.getAddress().getHostAddress();

            // Divide a mensagem em tuplas usando "*" como delimitador
            // O primeiro elemento será vazio devido ao "*" inicial, então começamos do índice 1
            String[] tuplas = mensagem.split("\\*");

            for (int i = 1; i < tuplas.length; i++)
            {
                String tupla = tuplas[i];

                // Verificar se a tupla não está vazia
                if (!tupla.isEmpty())
                {
                    // Dividir a tupla em IP e métrica usando ";" como delimitador
                    String[] partes = tupla.split(";");

                    if (partes.length == 2)
                    {
                        String ipDestino = partes[0];
                        int metrica = Integer.parseInt(partes[1]);

                        // Incrementar a métrica em 1 (custo para chegar ao vizinho)
                        int metricaTotal = metrica + 1;

                        // Criar a nova rota
                        Rota novaRota = new Rota(ipDestino, metricaTotal, ipProximoSalto);
                        novasRotas.add(novaRota);

                        System.out.println("[LOG] Rota parseada - Destino: " + ipDestino + " | Métrica: " + metricaTotal + " | Próximo Salto: " + ipProximoSalto);
                    }
                    else
                    {
                        System.err.println("[ERRO] Tupla com formato inválido: " + tupla);
                    }
                }
            }

            // Atualizar a tabela de roteamento com as novas rotas
            roteador.tabelaDeRoteamento.AtualizarRotas(novasRotas);
        }
        catch (Exception e)
        {
            System.err.println("[ERRO] Erro ao parsear mensagem de atualização: " + e.getMessage());
            e.printStackTrace();
        }
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