package Entidades;
import java.io.IOException;
import java.net.DatagramSocket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import Threads.ThreadDeRecepcao;
import Threads.ThreadDoTerminal;

public class Roteador
{
    private static final String NOME_DO_ARQUIVO = "roteadores.txt";
    private static final int PORTA_LOCAL = 6000;

    public String ip;
    public TabelaDeRoteamento tabelaDeRoteamento;
    public List<Nodo> roteadoresVizinhos;

    public Roteador(String ip)
    {
        this.ip = ip;
        tabelaDeRoteamento = new TabelaDeRoteamento();
        roteadoresVizinhos = new ArrayList<>();
    }

    public void Ligar()
    {
        // Descomentar quando implementar a leitura do arquivo de vizinhos
        List<String> vizinhosIniciais = LerTXTComVizinhos();
        //SeAnunciarParaOsVizinho(vizinhosIniciais);

        try
        {
            DatagramSocket socket = new DatagramSocket(PORTA_LOCAL);

            System.out.println("===================================================");
            System.out.println("Roteador iniciado em " + ip + ":" + PORTA_LOCAL);
            System.out.println("Comandos disponíveis:");
            System.out.println("  enviar <ip> <porta> <mensagem>");
            System.out.println("  sair");
            System.out.println("===================================================");

            Thread threadDeRecepcao = new Thread(new ThreadDeRecepcao(socket, this));
            Thread threadDoTerminal = new Thread(new ThreadDoTerminal(socket));

            threadDeRecepcao.start();
            threadDoTerminal.start();

            threadDoTerminal.join();

            socket.close();
            System.out.println("Roteador encerrado.");
            System.exit(0);

        } catch (Exception e) {
            System.err.println("Erro ao iniciar o roteador: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<String> LerTXTComVizinhos()
    {
        List<String> vizinhos = new ArrayList<>();
        try
        {
            vizinhos = Files.readAllLines(Paths.get(NOME_DO_ARQUIVO));
        }
        catch (IOException e)
        {
            System.err.println("Erro ao ler arquivo de vizinhos: " + e.getMessage());
        }
        return vizinhos;
    }

    // Como a professora disse em aula: QUANDO LER O ARQUIVO DE CONFIGURAÇÃO MANDAR O @ COM O PRÓPRIO IP PARA SE ANUNCIAR.
    public void SeAnunciarParaOsVizinho(List<String> vizinhosIniciais)
    {
        for (String vizinho : vizinhosIniciais)
        {
            // Enviar mensagem de anúncio para cada vizinho. Implementar a mensagem @
        }
    }
}
