package TesteRMI;

import java.rmi.RemoteException;

public class LoopServidor
{
    public static void main(String[] args) throws RemoteException
    {
        ImplServidor servidor = new ImplServidor("servidor");
    }
}
