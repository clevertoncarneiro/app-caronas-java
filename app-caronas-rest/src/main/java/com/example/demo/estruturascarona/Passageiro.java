package com.example.demo.estruturascarona;

import java.util.ArrayList;
import java.util.List;

// Informacoes pertinentes a um passageiro
public class Passageiro extends Registro 
{
    public List<Motorista> listaMotoristas = new ArrayList<>();

    public Passageiro(Registro novoRegistro) {
        this.id_registro = novoRegistro.id_registro;
        this.nome = novoRegistro.nome;
        this.contato = novoRegistro.contato;
        this.origem = novoRegistro.origem;
        this.destino = novoRegistro.destino;
        this.data = novoRegistro.data;
        this.flagNotificar = false;
    }
}
