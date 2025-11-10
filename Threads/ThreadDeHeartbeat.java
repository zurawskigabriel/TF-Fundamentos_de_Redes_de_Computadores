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
            // TODO: enviar,  a  cada  10  segundos,  mensagens  de  atualização  com  o conteúdo  da  tabela  de  roteamento
            // (apenas  os  campos  IP  de  Destino  e  Métrica)  para  seus vizinhos.

            // TODO: Verificar se  algum  vizinho (Nodo)  não  enviou  mensagens  de  heartbeat  nos últimos 15 segundos.
        }
    }
}
