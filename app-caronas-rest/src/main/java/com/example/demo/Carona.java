package com.example.demo;


public class Carona 
{
    private String nome;
    private String contato;
    private String origem;
    private String destino;
    private String data;
    private short passageiros;

    // ---------------- metodos get -------------------
    public String getNome() 
    {
        return nome;
    }

    public String getContato() 
    {
        return contato;
    }

    public String getOrigem() 
    {
        return origem;
    }

    public String getDestino() 
    {
        return destino;
    }

    public String getData() 
    {
        return data;
    }

    public short getPassageiros() 
    {
        return passageiros;
    }

    // ----------------- metodos set ---------------------
    public void setNome(String nome) 
    {
        this.nome = nome;
    }
    
    public void setContato(String contato) 
    {
        this.contato = contato;
    }

    public void setOrigem(String origem) 
    {
        this.origem = origem;
    }

    public void setDestino(String destino) 
    {
        this.destino = destino;
    }

    public void setData(String data) 
    {
        this.data = data;
    }

    public void setPassageiros(short passageiros) 
    {
        this.passageiros = passageiros;
    }
}