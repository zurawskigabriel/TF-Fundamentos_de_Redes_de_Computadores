package Entidades;

import java.util.ArrayList;
import java.util.List;

public class TabelaDeVizinhos
{
    public List<Vizinho> vizinhos;

    public TabelaDeVizinhos()
    {
        this.vizinhos = new ArrayList<>();
    }

    public void AdicionarVizinho(String ip)
    {
        Vizinho nodo = new Vizinho(ip);
        for (Vizinho n : vizinhos) if (n.ip.equals(ip)) return; // Se o vizinho já existe na tabela, não o adiciona novamente
        vizinhos.add(nodo);
    }

    public void RemoverVizinho(String ip)
    {
        // TODO: Remover vizinho
    }

    public void MostrarTabelaDeVizinhos()
    {
        System.out.println("Tabela de Vizinhos:");
        for (Vizinho vizinho : vizinhos)
        {
            System.out.println("- " + vizinho.ip);
        }
    }
}