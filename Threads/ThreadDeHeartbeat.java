package Threads;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Entidades.Roteador;
import Entidades.Vizinho;

public class ThreadDeHeartbeat implements Runnable
{
    private static final int INTERVALO_HEARTBEAT = 10000; // 10 segundos em milissegundos
    private static final int TIMEOUT_VIZINHO = 15000; // 15 segundos em milissegundos
    
    private Roteador roteador;

    public ThreadDeHeartbeat(Roteador roteador)
    {
        this.roteador = roteador;
    }

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                // Envia mensagens de atualização com o conteúdo da tabela de roteamento
                // (apenas os campos IP de Destino e Métrica) para seus vizinhos
                roteador.EnviarAtualizacaoDeTabelaParaTodosVizinhos();

                // Verifica se algum vizinho não enviou mensagens de heartbeat nos últimos 15 segundos
                VerificarVizinhosInativos();

                // Aguarda 10 segundos antes da próxima iteração
                Thread.sleep(INTERVALO_HEARTBEAT);
            }
            catch (InterruptedException e)
            {
                System.err.println("[ERRO] Thread de heartbeat interrompida: " + e.getMessage());
                break;
            }
            catch (Exception e)
            {
                System.err.println("[ERRO] Erro na thread de heartbeat: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void VerificarVizinhosInativos()
    {
        LocalDateTime agora = LocalDateTime.now();
        List<Vizinho> vizinhosInativos = new ArrayList<>();

        // Identifica vizinhos inativos
        for (Vizinho vizinho : roteador.tabelaDeVizinhos.vizinhos)
        {
            Duration duracao = Duration.between(vizinho.ultimoUpdate, agora);
            long milissegundos = duracao.toMillis();

            if (milissegundos > TIMEOUT_VIZINHO)
            {
                vizinhosInativos.add(vizinho);
            }
        }

        // Remove vizinhos inativos e suas rotas
        for (Vizinho vizinhoInativo : vizinhosInativos)
        {
            System.out.println("[LOG] Vizinho " + vizinhoInativo.ip + " inativo há mais de 15 segundos. Removendo...");
            
            // Remove todas as rotas que usam esse vizinho como próximo salto
            roteador.tabelaDeRoteamento.RemoverRotasPorProximoSalto(vizinhoInativo.ip);
            
            // Remove o vizinho da tabela de vizinhos
            roteador.tabelaDeVizinhos.RemoverVizinho(vizinhoInativo.ip);
        }
    }
}
