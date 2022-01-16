package com.example.demo.estruturascarona;

import java.util.ArrayList;
import java.util.List;

public class Servidor
{
    List<Passageiro> listaPassageiros = new ArrayList<>();
    List<Motorista> listaMotoristas = new ArrayList<>();

    // Retorna lista de caronas disponiveis com as informacoes passadas
    public List<String> consultarCaronas(String origem, String destino, String data)
    {
        System.out.println("Servidor: Nova consulta!");

        // pesquisar as infos passadas = retornar a lista com o que achou
        List<String> caronas = new ArrayList<>();

        for (Motorista motorista : listaMotoristas) 
        {
            if (motorista.data.equals(data) 
                && motorista.origem.equals(origem)
                && motorista.destino.equals(destino)) 
            {
                caronas.add("Motorista "
                            + motorista.nome + " com " 
                            + motorista.listaPassageirosDisponiveis.size() + "/" 
                            + motorista.numeroPassageiros 
                            + " passageiros.\n");
            }
        }

        for (Passageiro passageiro : listaPassageiros) 
        {
            if (passageiro.data.equals(data) 
                && passageiro.origem.equals(origem)
                && passageiro.destino.equals(destino)) 
            {
                caronas.add("Passageiro " + passageiro.nome + "!\n");
            }
        }

        // Se sem resultados
        if (caronas.size() == 0) {
            caronas.add("Nenhum resultado!\n");
        }

        return caronas;
    }

    // Registra o interesse e notifica caso encontre um registro ja existente
    public int registrarInteresse(String nome, String contato, String origem, 
                                  String destino, String data, int numPassageiros)
    {
        System.out.println("Servidor: Novo registro de Interesse!");

        // Preenche informacoes na estrutura
        Registro novoRegistro = new Registro();

        novoRegistro.nome = nome;
        novoRegistro.contato = contato;
        novoRegistro.origem = origem;
        novoRegistro.destino = destino;
        novoRegistro.data = data;
        // Gera hash como id unico
        novoRegistro.id_registro = novoRegistro.hashCode() % 0xFFFFFF; 

        // Decide se eh passageiro ou motorista
        if (numPassageiros < 1) 
        {
            Passageiro novoPassageiro = new Passageiro(novoRegistro);

            for (Motorista motorista : listaMotoristas) 
            {
                if (motorista.origem.equals(novoPassageiro.origem)
                    && motorista.destino.equals(novoPassageiro.destino)
                    && motorista.data.equals(novoPassageiro.data)) 
                {
                    motorista.listaPassageirosDisponiveis.add(novoPassageiro);
                    motorista.flagNotificar = true;

                    novoPassageiro.listaMotoristas.add(motorista);
                    novoPassageiro.flagNotificar = true;

                    break;
                }
            }

            listaPassageiros.add(novoPassageiro);
        } 
        else 
        {
            Motorista novoMotorista = new Motorista(novoRegistro, numPassageiros);

            for (Passageiro passageiro : listaPassageiros) 
            {
                if (passageiro.origem.equals(novoMotorista.origem)
                    && passageiro.destino.equals(novoMotorista.destino)
                    && passageiro.data.equals(novoMotorista.data)) 
                {
                    if (novoMotorista.listaPassageirosDisponiveis.size() == 
                        novoMotorista.numeroPassageiros)
                    {
                        continue;
                    }

                    novoMotorista.listaPassageirosDisponiveis.add(passageiro);
                    novoMotorista.flagNotificar = true;

                    passageiro.listaMotoristas.add(novoMotorista);
                    passageiro.flagNotificar = true;
                }
            }

            listaMotoristas.add(novoMotorista);
        }

        // Retorna ID
        return novoRegistro.id_registro;
    }

    // Cancela registro de interesse pelo ID
    public boolean cancelarRegistroInteresse(int IdInteresse) 
    {
        System.out.println("Servidor: Cancelamento de registro de Interesse!");

        // Verifica na lista de motoristas
        for (Motorista motorista : listaMotoristas) 
        {
            if (motorista.id_registro == IdInteresse) 
            {
                for (Passageiro passageiro : listaPassageiros)
                {
                    passageiro.listaMotoristas.remove(motorista);
                }

                listaMotoristas.remove(motorista);
                return true;
            }
        }

        // Verifica na lista de passageiros
        for (Passageiro passageiro : listaPassageiros) 
        {
            if (passageiro.id_registro == IdInteresse) 
            {
                for (Motorista motorista : listaMotoristas)
                {
                    motorista.listaPassageirosDisponiveis.remove(passageiro);
                }

                listaPassageiros.remove(passageiro);
                return true;
            }
        }

        // Caso nao achou registro
        return false;
    }

    // Retorna se deve notificar ou nao o cliente pelo ID do registro
    public boolean retornaFlagNotificao(int id_registro) 
    {
        boolean retorno = false;

        // Verifica na lista de motoristas
        for (Motorista motorista : listaMotoristas) 
        {
            // Se achou o motorista com o mesmo id
            if (motorista.id_registro == id_registro) 
            {
                retorno = motorista.flagNotificar;
                // Limpa flag
                motorista.flagNotificar = false;

                return retorno;
            }
        }

        // Verifica na lista de passageiros
        for (Passageiro passageiro : listaPassageiros) 
        {
            // Se achou o passageiro com o mesmo id
            if (passageiro.id_registro == id_registro) 
            {
                retorno = passageiro.flagNotificar;
                // Limpa flag
                passageiro.flagNotificar = false;

                return retorno;
            }
        }

        // Retorna se deve notificar o dono do id do registro
        return retorno;
    }
}