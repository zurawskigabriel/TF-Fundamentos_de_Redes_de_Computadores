package Entidades;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import Threads.ThreadDeHeartbeat;
import Threads.ThreadDeRecepcao;
import Threads.ThreadDoTerminal;

public class Roteador
{
    public static final String NOME_DO_ARQUIVO = "roteadores.txt";
    public static final int PORTA_LOCAL = 6000;

    public String ip;
    public TabelaDeRoteamento tabelaDeRoteamento;
    public TabelaDeVizinhos tabelaDeVizinhos;
    public DatagramSocket socket;
    public boolean hearbeatAtivado;

    public Roteador(String ip, boolean hearbeatAtivado)
    {
        this.ip = ip;
        this.hearbeatAtivado = hearbeatAtivado;
        tabelaDeRoteamento = new TabelaDeRoteamento();
        tabelaDeVizinhos = new TabelaDeVizinhos();
    }

    public void Ligar()
    {
        try
        {
            socket = new DatagramSocket(PORTA_LOCAL);

            System.out.println("===================================================");
            System.out.println("        TERMINAL DE COMANDOS DO ROTEADOR");
            System.out.println("===================================================");
            System.out.println("Roteador iniciado em " + ip + ":" + PORTA_LOCAL);
            System.out.println("Comandos disponíveis:");
            System.out.println("  enviar <ip> <mensagem>");
            System.out.println("  vizinhos");
            System.out.println("  rotas");
            System.out.println("  anunciar");
            System.out.println("  sair");
            System.out.println("===================================================");

            GerenciadorDeOutput.Log("===================================================");
            GerenciadorDeOutput.Log("Roteador iniciado em " + ip + ":" + PORTA_LOCAL);
            GerenciadorDeOutput.Log("===================================================");

            // Carrega vizinhos e envia anúncios (agora que o socket está pronto)
            LerTXTComVizinhos();

            Thread threadDeRecepcao = new Thread(new ThreadDeRecepcao(this));
            Thread threadDeHeartbeat = new Thread(new ThreadDeHeartbeat(this));
            Thread threadDoTerminal = new Thread(new ThreadDoTerminal(this));

            threadDeRecepcao.start();
            threadDeHeartbeat.start();
            threadDoTerminal.start();

            threadDoTerminal.join();

            socket.close();
            GerenciadorDeOutput.Log("[LOG] Roteador encerrado.");
            GerenciadorDeOutput.Desativar();
            System.out.println("[LOG] Roteador encerrado.");
            System.exit(0);

        } catch (Exception e) {
            System.err.println("[ERRO] Erro ao iniciar o roteador: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void LerTXTComVizinhos()
    {
        List<String> IPsDeVizinhos = new ArrayList<>();
        try
        {
            IPsDeVizinhos = Files.readAllLines(Paths.get(NOME_DO_ARQUIVO));
        }
        catch (IOException e)
        {
            GerenciadorDeOutput.Log("[ERRO] Erro ao ler arquivo de vizinhos: " + e.getMessage());
        }

        for (String ipVizinho : IPsDeVizinhos)
        {
            ipVizinho = ipVizinho.trim();

            // Ignora linhas vazias e o próprio IP
            if (ipVizinho.isEmpty() || ipVizinho.equals(this.ip))
            {
                continue;
            }

            tabelaDeVizinhos.AdicionarVizinho(ipVizinho);
        }

        GerenciadorDeOutput.Log("[LOG] " + tabelaDeVizinhos.vizinhos.size() + " vizinho(s) carregado(s) do arquivo.");
    }

    public void EnviarMensagemDeTexto(String ipDestino, String mensagem)
    {
        try
        {
            // Se a mensagem já está no formato correto (!<ipOrigem>;<ipDestino>;<texto>), envia diretamente
            if (mensagem.startsWith("!"))
            {
                // Mensagem já está formatada (está sendo reencaminhada)
                String[] partes = mensagem.substring(1).split(";");
                String ipDestinoReal = partes[1];

                // Procura a rota para o destino
                Rota rotaEncontrada = null;
                for (Rota rota : tabelaDeRoteamento.rotas)
                {
                    if (rota.ipEntrada.equals(ipDestinoReal))
                    {
                        rotaEncontrada = rota;
                        break;
                    }
                }

                if (rotaEncontrada == null)
                {
                    GerenciadorDeOutput.Log("[ERRO] Não há rota para o destino " + ipDestinoReal);
                    return;
                }

                // Envia para o próximo salto
                byte[] mensagemCodificada = mensagem.getBytes();
                InetAddress enderecoProximoSalto = InetAddress.getByName(rotaEncontrada.ipSaida);
                DatagramPacket pacote = new DatagramPacket(mensagemCodificada, mensagemCodificada.length, enderecoProximoSalto, PORTA_LOCAL);
                socket.send(pacote);

                GerenciadorDeOutput.Log("[ENVIADO !] | Reencaminhando para próximo salto: " + rotaEncontrada.ipSaida + " | Destino final: " + ipDestinoReal);
            }
            else
            {
                // Mensagem original (primeira vez enviando)
                // Procura a rota para o destino
                Rota rotaEncontrada = null;
                for (Rota rota : tabelaDeRoteamento.rotas)
                {
                    if (rota.ipEntrada.equals(ipDestino))
                    {
                        rotaEncontrada = rota;
                        break;
                    }
                }

                if (rotaEncontrada == null)
                {
                    System.out.println("[ERRO] Não há rota para o destino " + ipDestino);
                    return;
                }

                // Formata a mensagem: !<ipOrigem>;<ipDestino>;<texto>
                String mensagemFormatada = "!" + this.ip + ";" + ipDestino + ";" + mensagem;
                byte[] mensagemCodificada = mensagemFormatada.getBytes();

                // Envia para o próximo salto
                InetAddress enderecoProximoSalto = InetAddress.getByName(rotaEncontrada.ipSaida);
                DatagramPacket pacote = new DatagramPacket(mensagemCodificada, mensagemCodificada.length, enderecoProximoSalto, PORTA_LOCAL);
                socket.send(pacote);

                System.out.println("[ENVIADO !] | Para próximo salto: " + rotaEncontrada.ipSaida + " | Destino final: " + ipDestino + " | Mensagem: " + mensagem);
            }

        }
        catch (NumberFormatException e)
        {
            System.out.println("[ERRO] Porta inválida. Deve ser um número.");
        }
        catch (Exception e)
        {
            System.err.println("[ERRO] Erro ao enviar mensagem: " + e.getMessage());
        }
    }

    public void EnviarMensagemDeAnuncio(String ipDestino)
    {
        try
        {
            String mensagem = "@" + this.ip;
            byte[] buffer = mensagem.getBytes();

            InetAddress enderecoDestino = InetAddress.getByName(ipDestino);
            DatagramPacket pacote = new DatagramPacket(buffer, buffer.length, enderecoDestino, PORTA_LOCAL);

            socket.send(pacote);
            GerenciadorDeOutput.Log("[ENVIADO @] | Para: " + ipDestino + " | Mensagem: " + mensagem);
        }
        catch (Exception e)
        {
            GerenciadorDeOutput.Log("[ERRO] Falha ao enviar mensagem de anúncio para " + ipDestino + ": " + e.getMessage());
        }
    }

    public void SeAnunciarParaTodosVizinhos()
    {
        for (Vizinho vizinho : tabelaDeVizinhos.vizinhos)
        {
            EnviarMensagemDeAnuncio(vizinho.ip);
        }
    }

    public void EnviarAtualizacaoDeTabelaParaTodosVizinhos()
    {
        // Constrói a mensagem com todas as rotas
        StringBuilder mensagem = new StringBuilder("*");

        for (Rota rota : tabelaDeRoteamento.rotas)
        {
            mensagem.append("*").append(rota.ipEntrada).append(";").append(rota.metrica);
        }

        String mensagemFinal = mensagem.toString();
        byte[] buffer = mensagemFinal.getBytes();

        // Envia para todos os vizinhos
        for (Vizinho vizinho : tabelaDeVizinhos.vizinhos)
        {
            try
            {
                InetAddress enderecoDestino = InetAddress.getByName(vizinho.ip);
                DatagramPacket pacote = new DatagramPacket(buffer, buffer.length, enderecoDestino, PORTA_LOCAL);
                socket.send(pacote);
                GerenciadorDeOutput.Log("[ENVIADO *] | Para: " + vizinho.ip + " | Rotas: " + tabelaDeRoteamento.rotas.size());
            }
            catch (Exception e)
            {
                GerenciadorDeOutput.Log("[ERRO] Falha ao enviar atualização de tabela para " + vizinho.ip + ": " + e.getMessage());
            }
        }
    }
}
