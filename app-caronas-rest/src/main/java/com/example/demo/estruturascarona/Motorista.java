package com.example.demo.estruturascarona;

import java.util.ArrayList;
import java.util.List;

// Informacoes pertientes a um motorista
public class Motorista extends Registro 
{
    public int numeroPassageiros;
    public List<Passageiro> listaPassageirosDisponiveis = new ArrayList<>();

    public Motorista(Registro novoRegistro, int numeroPassageiros) 
    {
        this.id_registro = novoRegistro.id_registro;
        this.nome = novoRegistro.nome;
        this.contato = novoRegistro.contato;
        this.origem = novoRegistro.origem;
        this.destino = novoRegistro.destino;
        this.data = novoRegistro.data;
        this.numeroPassageiros = numeroPassageiros;
        this.flagNotificar = false;
    }
}
