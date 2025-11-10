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
        Rota rota = new Rota(ipDestino, metrica, ipSaida);
        rotas.add(rota);
    }

    public void RemoverRota()
    {
        // TODO: Implementar a remoção de uma rota
    }

    public void AtualizarRotas(List<Rota> novasRotas)
    {
        // TODO: Implementar a atualização das rotas
    }
}
