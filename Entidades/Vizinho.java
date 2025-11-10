package Entidades;

import java.time.LocalDateTime;

public class Vizinho
{
    public String ip;
    public LocalDateTime ultimoUpdate;

    public Vizinho(String ip)
    {
        this.ip = ip;
        this.ultimoUpdate = LocalDateTime.now();
    }
}
