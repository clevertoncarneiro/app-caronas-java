package AppCaronas;

import TesteRMI.InterfaceCliente;
import java.security.PrivateKey;
import java.security.PublicKey;

// Informacoes principais do cliente
public class Cliente
{
    public InterfaceCliente refCliente;

    public String nome;
    public String contato;

    public PublicKey chavePublica;
    public PrivateKey chavePrivada;
}
