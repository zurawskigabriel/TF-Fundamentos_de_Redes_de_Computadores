package Entidades;

import java.time.LocalDateTime;

public class Rota
{
    public String ipEntrada;
    public int metrica;
    public String ipSaida;

    public Rota(String ipEntrada, int metrica, String ipSaida)
    {
        this.ipEntrada = ipEntrada;
        this.metrica = metrica;
        this.ipSaida = ipSaida;
    }
}
