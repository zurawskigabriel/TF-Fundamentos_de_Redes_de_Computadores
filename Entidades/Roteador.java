package Entidades;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

import Threads.ThreadDeRecepcao;
import Threads.ThreadDoTerminal;

public class Roteador
{
    private static final int PORTA_LOCAL = 6000;

    public TabelaDeRoteamento tabelaDeRoteamento;
    public List<Nodo> roteadoresVizinhos;

    public Roteador()
    {
        tabelaDeRoteamento = new TabelaDeRoteamento();
        roteadoresVizinhos = new ArrayList<>();
    }

    public void Ligar()
    {
        // Descomentar quando implementar a leitura do arquivo de vizinhos
        //List<String> vizinhosIniciais = LerTXTComVizinhos();
        //SeAnunciarParaOsVizinho(vizinhosIniciais);

        try
        {
            DatagramSocket socket = new DatagramSocket(PORTA_LOCAL);

            System.out.println("===================================================");
            System.out.println("Roteador iniciado na porta " + PORTA_LOCAL);
            System.out.println("Comandos disponíveis:");
            System.out.println("  enviar <ip> <porta> <mensagem>");
            System.out.println("  sair");
            System.out.println("===================================================");

            Thread threadDeRecepcao = new Thread(new ThreadDeRecepcao(socket));
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
        return null;
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
