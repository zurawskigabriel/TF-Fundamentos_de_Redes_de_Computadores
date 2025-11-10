import java.net.InetAddress;
import java.net.UnknownHostException;

import Entidades.Roteador;

public class Main
{
    public static void main(String[] args)
    {
        String ip = "";
        try
        {
            InetAddress enderecoLocal = InetAddress.getLocalHost();
            ip = enderecoLocal.getHostAddress();
        }
        catch (UnknownHostException e)
        {
            System.err.println("Erro ao obter IP: " + e.getMessage() + ". Fechando aplicação.");
            System.exit(1);
        }

        Roteador roteador = new Roteador(ip);
        roteador.Ligar();
    }
}