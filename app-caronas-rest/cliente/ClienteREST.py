import requests
import sseclient  # sseclient-py
import ctypes

# Defines
URL = 'http://localhost:8080/carona'
PATH_REGISTRO = '/registro'
PATH_CONSULTA = '/consulta'
PATH_DELETAR = '/delete/'
PATH_SSE = "/registro/sse/"


# POST para registrar a carona
def POST_registro(json_registro):
    # especifica header
    header = {'Content-type': 'application/json'}

    # Envia o json do POST
    resposta = requests.post(URL + PATH_REGISTRO, json=json_registro, headers=header)

    # Se obteve sucesso, retorna o id, ou -1 se falhou
    if resposta.status_code == 200:
        return resposta.json()['id']
    else:
        return -1

# POST para consultar uma carona
def POST_consulta_registros(json_consulta):
    # especifica header
    header = {'Content-type': 'application/json'}

    # Envia o json do POST
    resposta = requests.post(URL + PATH_CONSULTA, json=json_consulta, headers=header)

    # Se obteve sucesso, retorna os resultados, ou -1 se falhou
    if resposta.status_code == 200:
        return resposta.json()
    else:
        return -1

# DELETE do registro feito pelo id
def DELETE_registro(id):
    # Envia o json do DELETE
    resposta = requests.delete(URL + PATH_DELETAR + str(id))

    # Retorna resultado dependendo da resposta do servidor
    if resposta.status_code == 200:
        return 'Removido'
    elif resposta.status_code == 404:
        return 'Nao encontrado'
    else:
        return 'Erro'

# GET das notificacoes SSE com base no caminho
def GET_notificacoes_sse(id, nome, origem, destino, data):
    # Monta url
    url_sse = URL + PATH_SSE + str(id) + "+" + nome + "+" + \
        origem + "+" + destino + "+" + data.replace("/", "")
    
    # Abre comunicacao com o cliente
    resposta = requests.get(url_sse, stream=True)
    cliente = sseclient.SSEClient(resposta)

    try:
        # Loop enquanto a conexao estiver aberta
        for evento in cliente.events():
            showPopup('Match encontrado!', evento.data)
    except:
        print("Conexao SSE fechada!")

# JSON no formato de consulta
def JSON_consulta(origem, destino, data):
    json_envio = {
        'origem': origem,
        'destino': destino,
        'data': data
    }

    return json_envio

# JSON no formato de registro
def JSON_registro(nome, contato, origem, destino, data, pasNum):
    json_envio = {
        'nome': nome,
        'contato': contato,
        'origem': origem,
        'destino': destino,
        'data': data,
        'passageiros': int(pasNum)
    }

    return json_envio

# Mostra um popup com uma msgm
def showPopup(title, text):
    ctypes.windll.user32.MessageBoxW(0, text, title, 1)
