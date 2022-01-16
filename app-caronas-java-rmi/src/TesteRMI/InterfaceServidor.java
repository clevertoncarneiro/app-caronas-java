package TesteRMI;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PublicKey;
import java.util.List;

public interface InterfaceServidor extends Remote
{
    boolean cadastrarUsuario(String nome, String contato, PublicKey chavePublica) throws RemoteException;
    List<String> consultarCaronas(String origem, String destino, String data) throws RemoteException;
    int registrarInteresse(InterfaceCliente refCliente, String informacoes, byte[] assinatura) throws RemoteException;
    boolean cancelarRegistroInteresse(int IdInteresse) throws RemoteException;
}
