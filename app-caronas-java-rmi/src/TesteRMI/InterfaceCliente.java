package TesteRMI;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceCliente extends Remote
{
    String novaNotificacaoEvento(String msg) throws RemoteException;
}
