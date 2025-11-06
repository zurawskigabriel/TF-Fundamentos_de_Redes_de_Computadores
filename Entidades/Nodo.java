package Entidades;

import java.time.LocalDateTime;

public class Nodo
{
    public String IPgateway;
    public LocalDateTime ultimoUpdate;

    public Nodo(String IPgateway)
    {
        this.IPgateway = IPgateway;
        this.ultimoUpdate = LocalDateTime.now();
    }
}
