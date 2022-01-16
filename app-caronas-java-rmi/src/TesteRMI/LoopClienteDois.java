package TesteRMI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

public class LoopClienteDois
{
    static String retornaInputConsole()
    {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    public static void main(String[] args) throws NotBoundException, RemoteException
    {
        int estado = 0;
        boolean trocaTela = false;

        System.out.println("1. Cadastrar Usuario");
        retornaInputConsole();

        System.out.print("\nInsira seu nome: ");
        ImplCliente novoCliente = new ImplCliente(retornaInputConsole());

        System.out.print("\nInsira seu telefone: ");
        novoCliente.cliente.contato = retornaInputConsole();
        novoCliente.servidor.cadastrarUsuario(novoCliente.cliente.nome, novoCliente.cliente.contato, novoCliente.cliente.chavePublica);

        while (true)
        {
            // menu principal
            if (estado == 0)
            {
                System.out.println("1. Consultar Carona");
                System.out.println("2. Registrar Evento");
                System.out.println("3. Cancelar Registro de Evento");

                estado = Integer.parseInt(retornaInputConsole());

                trocaTela = true;
            }
            else if (estado == 1)
            {
                System.out.print("\nInsira o local origem: ");
                String origem = retornaInputConsole();

                System.out.print("\nInsira o local destino: ");
                String destino = retornaInputConsole();

                System.out.print("\nInsira a data da viagem: ");
                String data = retornaInputConsole();

                List<String> resultados = novoCliente.servidor.consultarCaronas(origem, destino, data);

                for (String linha:resultados)
                {
                    System.out.println(linha);
                }

                estado = 0;
            }
            else if (estado == 2)
            {
                System.out.print("\nInsira o local origem: ");
                String origem = retornaInputConsole();

                System.out.print("\nInsira o local destino: ");
                String destino = retornaInputConsole();

                System.out.print("\nInsira a data da viagem: ");
                String data = retornaInputConsole();

                System.out.print("\nInsira o numero de passageiros: ");
                String pasNum = retornaInputConsole();

                String msg = novoCliente.cliente.nome + "\n";
                msg += novoCliente.cliente.contato + "\n";
                msg += origem + "\n";
                msg += destino + "\n";
                msg += data + "\n";
                msg += pasNum + "\n";

                int id = novoCliente.servidor.registrarInteresse(novoCliente,
                        msg, novoCliente.assinarMsg(msg));

                System.out.println("\nSeu id de registro: " + id);

                estado = 0;
            }
            else if (estado == 3)
            {
                System.out.print("Insira o id da viagem: ");
                int id = Integer.parseInt(retornaInputConsole());

                boolean result = novoCliente.servidor.cancelarRegistroInteresse(id);

                System.out.println("\nA operação foi ..." + result);

                estado = 0;
            }

            if (trocaTela)
            {
                System.out.print("\033[H\033[2J");
                System.out.flush();
                trocaTela = false;
            }
        }
    }
}
