package Threads;

import Entidades.Roteador;

public class ThreadDeHeartbeat implements Runnable
{
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
            // TODO: Implementar a lógica de atualização da tabela de roteamento
        }
    }
}
