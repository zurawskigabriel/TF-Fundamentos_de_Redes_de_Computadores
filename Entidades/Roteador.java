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

    public Roteador(String ip)
    {
        this.ip = ip;
        tabelaDeRoteamento = new TabelaDeRoteamento();
        tabelaDeVizinhos = new TabelaDeVizinhos();
    }

    public void Ligar()
    {
        try
        {
            socket = new DatagramSocket(PORTA_LOCAL);

            System.out.println("===================================================");
            System.out.println("Roteador iniciado em " + ip + ":" + PORTA_LOCAL);
            System.out.println("Comandos disponíveis:");
            System.out.println("  enviar <ip> <mensagem>");
            System.out.println("  sair");
            System.out.println("===================================================");

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
            System.err.println("[ERRO] Erro ao ler arquivo de vizinhos: " + e.getMessage());
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
            tabelaDeRoteamento.AdicionarRota(ipVizinho, 1, ipVizinho);
            EnviarMensagemDeAnuncio(ipVizinho);
        }

        System.out.println("[LOG] " + tabelaDeVizinhos.vizinhos.size() + " vizinho(s) carregado(s) do arquivo.");
    }

    public void EnviarMensagemDeTexto(String ipDestino, String mensagem)
    {
        try
        {
            // Converte a mensagem em bytes
            byte[] mensagemCodificada = mensagem.getBytes();

            // Cria o pacote UDP
            InetAddress enderecoDestino = InetAddress.getByName(ipDestino);
            DatagramPacket pacote = new DatagramPacket(mensagemCodificada, mensagemCodificada.length, enderecoDestino, PORTA_LOCAL);

            // TODO: Procura o próximo salto na tabela de roteamento

            // TODO: Envia o pacote para o próximo salto


            System.out.println("[LOG] Mensagem \"" + mensagem + "\" enviada para " + ipDestino + ":" + PORTA_LOCAL);

        } catch (NumberFormatException e) {
            System.out.println("[ERRO] Porta inválida. Deve ser um número.");
        } catch (Exception e) {
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
            System.out.println("[ENVIADO @] | Para: " + ipDestino + " | Mensagem: " + mensagem);
        }
        catch (Exception e)
        {
            System.err.println("[ERRO] Falha ao enviar mensagem de anúncio para " + ipDestino + ": " + e.getMessage());
        }
    }
}
