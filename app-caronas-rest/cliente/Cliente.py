import os
from threading import Thread

from ClienteREST import *

# defines
TELA_PRINCIPAL = 0
TELA_CONSULTA  = 1
TELA_REGISTRO  = 2
TELA_DELETAR   = 3

# Abre uma interacao com o cliente com input
def msgm_com_input(texto):
    print("\n" + texto + ": ")
    return str(input())

# Cria uma nova thread para tratar o sse
def nova_thread_sse(id, nome, origem, destino, data):
    thread = Thread(target=GET_notificacoes_sse,
                    args=(id, nome, origem, destino, data,))
    thread.start()

def main():
    # Inicializa estruturas
    tela_atual = TELA_PRINCIPAL
    limpar_tela = False

    while True:
        if tela_atual == TELA_PRINCIPAL:
            print("1. Consultar Carona")
            print("2. Registrar Evento")
            print("3. Cancelar Registro de Evento")

            tela_atual = int(input())

            limpar_tela = True

        elif tela_atual == TELA_CONSULTA:
            # Pega informacoes
            origem  = msgm_com_input("Insira o local origem")
            destino = msgm_com_input("Insira o local destino")
            data    = msgm_com_input("Insira a data da viagem")

            # Monta formato do json de envio
            json_envio = JSON_consulta(origem, destino, data)

            # Imprime resultados da consulta
            resultados = POST_consulta_registros(json_envio)
            for linha in resultados:
                print(linha)

            # Volta para tela principal
            tela_atual = TELA_PRINCIPAL

        elif tela_atual == TELA_REGISTRO:
            # Pega informacoes
            nome    = msgm_com_input("Insira o seu nome")
            contato = msgm_com_input("Insira o seu contato")
            origem  = msgm_com_input("Insira o local origem")
            destino = msgm_com_input("Insira o local destino")
            data    = msgm_com_input("Insira a data da viagem")
            pasNum  = msgm_com_input("Insira o numero de passageiros")
            
            # Monta formato do json de envio
            json_envio = JSON_registro(nome, contato, origem, destino, data, pasNum)

            # Imprime id retornado pelo novo registro
            id = POST_registro(json_envio)
            print("\nSeu id de registro: " + str(id))

            # Abre conexao SSE
            nova_thread_sse(id, nome, origem, destino, data)

            # Volta para tela principal
            tela_atual = TELA_PRINCIPAL

        elif tela_atual == TELA_DELETAR:
            # Pega id para ser removido
            id = msgm_com_input("Insira o id da viagem")

            # Imprime resultado da operacao
            print(DELETE_registro(id))

            # Volta para tela principal
            tela_atual = TELA_PRINCIPAL


        if limpar_tela:
            os.system('cls' if os.name == 'nt' else 'clear')
            limpar_tela = False
            


if __name__ == "__main__":
    main()
