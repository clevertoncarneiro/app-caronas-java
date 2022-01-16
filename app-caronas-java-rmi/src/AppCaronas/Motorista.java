package AppCaronas;

// Informacoes pertientes a um motorista
public class Motorista extends Registro
{
    public int numeroPassageiros;

    public Motorista(Registro novoRegistro, int numeroPassageiros)
    {
        this.refCliente = novoRegistro.refCliente;
        this.IdRegistro = novoRegistro.IdRegistro;
        this.nomeSolicitante = novoRegistro.nomeSolicitante;
        this.contatoSolicitante = novoRegistro.contatoSolicitante;
        this.origemSolicitante = novoRegistro.origemSolicitante;
        this.destinoSolicitante = novoRegistro.destinoSolicitante;
        this.dataViagemSolicitante = novoRegistro.dataViagemSolicitante;
        this.numeroPassageiros = numeroPassageiros;
    }
}
