package TesteRMI;
import AppCaronas.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;


public class ImplServidor extends UnicastRemoteObject implements InterfaceServidor
{
    public String nomeServidor;
    List<Cliente> listaClientes = new ArrayList<>();
    List<Passageiro> listaPassageiros = new ArrayList<>();
    List<Motorista> listaMotoristas = new ArrayList<>();

    public ImplServidor(String nomeServidor) throws RemoteException
    {
        this.nomeServidor = nomeServidor;

        // Cria o registro e adiciona o servidor criado
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind(nomeServidor, this);


        System.out.println("Servidor " + nomeServidor + " criado...");
    }

    // Cadastra o novo usuario
    public boolean cadastrarUsuario(String nome, String contato, PublicKey chavePublica) throws RemoteException
    {
        System.out.println("Servidor: Novo cliente registrado: " + nome);

        // Guardar as informacoes
        Cliente novoCliente = new Cliente();

        novoCliente.nome = nome;
        novoCliente.contato = contato;
        novoCliente.chavePublica = chavePublica;

        this.listaClientes.add(novoCliente);

        return true;
    }

    // Retorna lista de caronas disponiveis com as informacoes passadas
    public List<String> consultarCaronas(String origem, String destino, String data) throws RemoteException
    {
        System.out.println("Servidor: Nova consulta!");

        // pesquisar as infos passadas = retornar a lista com o que achou
        List<String> caronas = new ArrayList<>();

        for (Motorista motorista: listaMotoristas)
        {
            if (motorista.dataViagemSolicitante.equals(data) &&
                    motorista.origemSolicitante.equals(origem) &&
                    motorista.destinoSolicitante.equals(destino))
            {
                caronas.add(motorista.nomeSolicitante + " com " + motorista.numeroPassageiros + " passageiros.\n");
            }
        }

        // Se sem resultados
        if (caronas.size() == 0)
        {
            caronas.add("Nenhum resultado!\n");
        }

        return caronas;
    }

    // Registra o interesse e notifica caso encontre um registro já existente
    public int registrarInteresse(InterfaceCliente refCliente, String informacoes, byte[] assinatura) throws RemoteException
    {
        System.out.println("Servidor: Novo registro de Interesse!");

        // Parse nas informacoes coletadas
        List<String> lines = new ArrayList<>();
        informacoes.lines().forEach(lines::add);

        // Verifica validade da assinatura
        Assinatura novaAssinatura = new Assinatura();

        boolean ehValido = novaAssinatura.verificarAssinatura(informacoes.getBytes(),
                assinatura,
                retornaClientePorNome(lines.get(0)).chavePublica);

        if (!ehValido)
        {
            System.out.println("Não foi possivel validar assinatura!");
            return -1;
        }
        else
        {
            System.out.println("Assinatura valida!");
        }


        // Preenche informacoes na estrutura
        Registro novoRegistro = new Registro();

        novoRegistro.refCliente = refCliente;
        novoRegistro.nomeSolicitante = lines.get(0);
        novoRegistro.contatoSolicitante = lines.get(1);
        novoRegistro.origemSolicitante = lines.get(2);
        novoRegistro.destinoSolicitante = lines.get(3);
        novoRegistro.dataViagemSolicitante = lines.get(4);
        novoRegistro.IdRegistro = novoRegistro.hashCode() % 0xFFFF; // Gera hash como id unico

        // Decide se eh passageiro ou motorista
        int numPassageiros = Integer.parseInt(lines.get(5));
        if(numPassageiros < 1)
        {
            Passageiro novoPassageiro = new Passageiro(novoRegistro);
            listaPassageiros.add(novoPassageiro);

            for (Motorista motorista:listaMotoristas)
            {
                if (motorista.origemSolicitante.equals(novoPassageiro.origemSolicitante) &&
                    motorista.destinoSolicitante.equals(novoPassageiro.destinoSolicitante) &&
                    motorista.dataViagemSolicitante.equals(novoPassageiro.dataViagemSolicitante))
                {
                    novoPassageiro.refCliente.novaNotificacaoEvento("Motorista encontrado!");
                    motorista.refCliente.novaNotificacaoEvento("Passageiro encontrado!");

                    break;
                }
            }
        }
        else
        {
            Motorista novoMotorista = new Motorista(novoRegistro, numPassageiros);
            listaMotoristas.add(novoMotorista);

            for (Passageiro passageiro:listaPassageiros)
            {
                if (passageiro.origemSolicitante.equals(novoMotorista.origemSolicitante) &&
                        passageiro.destinoSolicitante.equals(novoMotorista.destinoSolicitante) &&
                        passageiro.dataViagemSolicitante.equals(novoMotorista.dataViagemSolicitante))
                {
                    novoMotorista.refCliente.novaNotificacaoEvento("Passageiro encontrado!");
                    passageiro.refCliente.novaNotificacaoEvento("Motorista encontrado!");

                    break;
                }
            }
        }

        // Retorna ID
        return novoRegistro.IdRegistro;
    }

    // Cancela registro de interesse pelo ID
    public boolean cancelarRegistroInteresse(int IdInteresse) throws RemoteException
    {
        System.out.println("Servidor: Cancelamento de registro de Interesse!");

        // Verifica na lista de motoristas
        for (Motorista motorista:listaMotoristas)
        {
            if (motorista.IdRegistro == IdInteresse)
            {
                listaMotoristas.remove(motorista);
                return true;
            }
        }

        // Verifica na lista de passageiros
        for (Passageiro passageiro:listaPassageiros)
        {
            if (passageiro.IdRegistro == IdInteresse)
            {
                listaPassageiros.remove(passageiro);
                return true;
            }
        }

        // Caso nao achou registro
        return false;
    }

    // AUX: pela referencia do cliente retorna o objeto cliente do servidor
    public Cliente retornaClientePorNome(String nomeCliente)
    {
        for (Cliente cliente:listaClientes)
        {
            if (cliente.nome.equals(nomeCliente))
            {
                return cliente;
            }
        }

        return null;
    }
}