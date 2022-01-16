package com.example.demo;

// imports especificos do java
import java.util.List;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// imports especificos do spring
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

// import de estruturas do projeto 1
import com.example.demo.estruturascarona.*;
@RestController
public class CaronaController 
{
    // Estrutura de controle da aplicacao vinda do projeto 1
    Servidor servidor = new Servidor();

    // POST que: recebe json de consulta
    //           retorna json de maths como resposta
    @RequestMapping(value = "/carona/consulta", method = RequestMethod.POST, 
                    produces = "application/json", consumes = "application/json")
    public List<String> Post(@RequestBody ConsultaCarona nova_consulta) 
    {  
        return servidor.consultarCaronas(   nova_consulta.getOrigem(), 
                                            nova_consulta.getDestino(), 
                                            nova_consulta.getData());
    }

    // POST que: recebe json de registro da carona
    //           retorna json com id unico do registro
    @RequestMapping(value = "/carona/registro", method = RequestMethod.POST,
                    produces = "application/json", consumes = "application/json")
    public Map<String, Long> Post(@RequestBody Carona carona)
    {
        long id =   servidor.registrarInteresse(carona.getNome(), 
                                                carona.getContato(), 
                                                carona.getOrigem(), 
                                                carona.getDestino(), 
                                                carona.getData(), 
                                                carona.getPassageiros());

        return Collections.singletonMap("id", id);
    }

    // DELETE que: recebe id do registro a ser deletado
    //             retorna resultado da operação
    @RequestMapping(value = "/carona/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> Delete(@PathVariable(value = "id") int id) 
    {
        // Tenta remover o registro e retorna se a acao foi concluida
        if (servidor.cancelarRegistroInteresse(id)) 
        {
            return new ResponseEntity<>(HttpStatus.OK);
        } 
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // GET que: recebe caminho com as info de pesquisa para abrir o SSE
    //          retorna msg de aviso de um novo match
    @GetMapping("/carona/registro/sse/{id}+{nome}+{origem}+{destino}+{data}")
    public SseEmitter streamSseMvc(@PathVariable(value = "id") int id,
                                   @PathVariable(value = "nome") String nome,
                                   @PathVariable(value = "origem") String origem,
                                   @PathVariable(value = "destino") String destino,
                                   @PathVariable(value = "data") String data) 
    {
        SseEmitter emitter = new SseEmitter((long) (60000 * 15));
        ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();

        // Inicia uma execucao de servico
        sseMvcExecutor.execute(() -> {
            try 
            {
                for (int i = 0; true; i++) 
                {
                    // Se obteve um match, avisa o cliente
                    if (servidor.retornaFlagNotificao(id)) 
                    {
                        // Instancia evento SSE
                        SseEventBuilder event = SseEmitter.event()
                                .data("Novo Match de viagem " + nome +"!")
                                .id(String.valueOf(i))
                                .name("sse event - mvc");
                        
                        // Envia msgm
                        emitter.send(event);

                        // fecha a conexao
                        // emitter.complete();
                        // break;
                    }

                    // Verifica a cada 1 segundo
                    Thread.sleep(1000);
                }
            } catch (Exception ex) 
            {
                emitter.completeWithError(ex);
            }
        });

        return emitter;
    }
}