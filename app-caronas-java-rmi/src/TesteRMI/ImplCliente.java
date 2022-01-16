package TesteRMI;

import AppCaronas.Assinatura;
import AppCaronas.Cliente;

import javax.swing.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.KeyPair;


public class ImplCliente extends UnicastRemoteObject implements InterfaceCliente
{
    public Cliente cliente = new Cliente();
    public InterfaceServidor servidor;
    public Assinatura assinatura = new Assinatura();

    public ImplCliente(String nomeCliente) throws RemoteException, NotBoundException
    {
        this.cliente.nome = nomeCliente;

        // Recupera registro ja criado pelo servidor
        Registry registry = LocateRegistry.getRegistry();

        // Cria registro na tabela para o novo cliente
        registry.rebind(nomeCliente, this);

        // Passa para o servidor a propria referencia
        this.servidor = (InterfaceServidor) registry.lookup("servidor");

        // Gera e seta chaves
        KeyPair parChaves = assinatura.gerarChaves();
        this.cliente.chavePublica = parChaves.getPublic();
        this.cliente.chavePrivada = parChaves.getPrivate();
    }

    // Recebe notificacao em forma de um aviso flutuante
    @Override
    public String novaNotificacaoEvento(String msgRecebida)
    {
        JOptionPane.showMessageDialog(null, this.cliente.nome + ": " + msgRecebida);

        return "Ok";
    }

    // Gera assinatura de uma entrada qualquer
    byte[] assinarMsg(String msg)
    {
        return assinatura.gerarAssinatura(msg.getBytes(), cliente.chavePrivada);
    }
}
