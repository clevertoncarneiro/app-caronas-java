package AppCaronas;

// Informacoes pertinentes a um passageiro
public class Passageiro extends Registro
{
    public Passageiro(Registro novoRegistro)
    {
        this.refCliente = novoRegistro.refCliente;
        this.IdRegistro = novoRegistro.IdRegistro;
        this.nomeSolicitante = novoRegistro.nomeSolicitante;
        this.contatoSolicitante = novoRegistro.contatoSolicitante;
        this.origemSolicitante = novoRegistro.origemSolicitante;
        this.destinoSolicitante = novoRegistro.destinoSolicitante;
        this.dataViagemSolicitante = novoRegistro.dataViagemSolicitante;
    }
}
