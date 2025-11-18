package Entidades;

import java.util.ArrayList;
import java.util.List;

public class TabelaDeRoteamento
{
    public List<Rota> rotas;

    public TabelaDeRoteamento()
    {
        this.rotas = new ArrayList<>();
    }

    public void AdicionarRota(String ipDestino, int metrica, String ipSaida)
    {
        // Verifica se já existe uma rota para esse destino
        for (Rota rota : rotas)
        {
            if (rota.ipEntrada.equals(ipDestino))
            {
                // Se já existe, não adiciona novamente
                return;
            }
        }
        
        Rota rota = new Rota(ipDestino, metrica, ipSaida);
        rotas.add(rota);
    }

    public void RemoverRota(String ipDestino)
    {
        rotas.removeIf(rota -> rota.ipEntrada.equals(ipDestino));
        GerenciadorDeOutput.Log("[LOG] Rota para " + ipDestino + " removida da tabela de roteamento.");
    }

    public void RemoverRotasPorProximoSalto(String ipProximoSalto)
    {
        List<Rota> rotasRemovidas = new ArrayList<>();
        rotas.removeIf(rota -> {
            if (rota.ipSaida.equals(ipProximoSalto)) {
                rotasRemovidas.add(rota);
                return true;
            }
            return false;
        });
        
        if (!rotasRemovidas.isEmpty()) {
            GerenciadorDeOutput.Log("[LOG] " + rotasRemovidas.size() + " rota(s) removida(s) devido à falha do próximo salto " + ipProximoSalto);
        }
    }

    public void AtualizarRotas(List<Rota> novasRotas)
    {
        for (Rota novaRota : novasRotas)
        {
            // Ignora rota para si mesmo
            if (novaRota.ipEntrada.equals(novaRota.ipSaida))
            {
                continue;
            }

            // Procura se já existe uma rota para esse destino
            Rota rotaExistente = null;
            for (Rota rota : rotas)
            {
                if (rota.ipEntrada.equals(novaRota.ipEntrada))
                {
                    rotaExistente = rota;
                    break;
                }
            }

            if (rotaExistente == null)
            {
                // Se não existe rota para esse destino, adiciona a nova rota
                rotas.add(novaRota);
                GerenciadorDeOutput.Log("[LOG] Nova rota adicionada - Destino: " + novaRota.ipEntrada + 
                                   " | Métrica: " + novaRota.metrica + 
                                   " | Próximo Salto: " + novaRota.ipSaida);
            }
            else
            {
                // Se já existe, verifica se a nova rota tem métrica menor
                if (novaRota.metrica < rotaExistente.metrica)
                {
                    GerenciadorDeOutput.Log("[LOG] Rota atualizada - Destino: " + novaRota.ipEntrada + 
                                       " | Métrica antiga: " + rotaExistente.metrica + 
                                       " | Métrica nova: " + novaRota.metrica + 
                                       " | Próximo Salto: " + novaRota.ipSaida);
                    rotaExistente.metrica = novaRota.metrica;
                    rotaExistente.ipSaida = novaRota.ipSaida;
                }
                // Se a métrica é igual ou maior, não atualiza (mas atualiza se vier do mesmo próximo salto)
                else if (rotaExistente.ipSaida.equals(novaRota.ipSaida))
                {
                    // Atualiza a métrica mesmo que seja maior, pois vem do mesmo próximo salto
                    if (novaRota.metrica != rotaExistente.metrica)
                    {
                        GerenciadorDeOutput.Log("[LOG] Rota atualizada (mesmo próximo salto) - Destino: " + novaRota.ipEntrada + 
                                           " | Métrica antiga: " + rotaExistente.metrica + 
                                           " | Métrica nova: " + novaRota.metrica);
                        rotaExistente.metrica = novaRota.metrica;
                    }
                }
            }
        }
    }

    public void MostrarTabelaDeRotas()
    {
        System.out.println("Tabela de Roteamento:");
        for (Rota rota : rotas)
        {
            System.out.println("- Destino: " + rota.ipEntrada + ", Métrica: " + rota.metrica + ", Próximo Salto: " + rota.ipSaida);
        }
    }
}
