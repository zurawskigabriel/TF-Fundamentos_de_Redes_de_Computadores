# Roteador - Sistema de Roteamento Distribuído

## Descrição

Este projeto implementa um sistema de roteamento distribuído em Java, simulando o comportamento de roteadores em uma rede. O sistema utiliza o algoritmo de vetor de distância (Distance Vector Routing) para calcular as melhores rotas entre os nós da rede.

## Funcionalidades

- **Protocolo de Vetor de Distância**: Implementação do algoritmo Distance Vector para roteamento dinâmico
- **Descoberta de Vizinhos**: Detecção automática de roteadores vizinhos na rede
- **Atualização Dinâmica de Rotas**: Troca periódica de tabelas de roteamento entre vizinhos
- **Detecção de Falhas**: Identificação automática de vizinhos inativos (timeout de 15 segundos)
- **Encaminhamento de Mensagens**: Envio de mensagens texto entre roteadores através de múltiplos saltos
- **Interface Dual**: Terminal separado para comandos e logs, facilitando a interação

## Arquitetura

O projeto está organizado em:

### Estrutura de Pacotes

```
├── Main.java                      # Ponto de entrada da aplicação
├── TerminalDeLogs.java            # Terminal dedicado para exibição de logs
├── Entidades/
│   ├── GerenciadorDeOutput.java   # Gerencia a saída de logs para terminal separado
│   ├── Rota.java                  # Representa uma rota na tabela de roteamento
│   ├── Roteador.java              # Classe principal do roteador
│   ├── TabelaDeRoteamento.java    # Gerencia a tabela de rotas
│   ├── TabelaDeVizinhos.java      # Gerencia a lista de vizinhos
│   └── Vizinho.java               # Representa um roteador vizinho
└── Threads/
    ├── ThreadDeHeartbeat.java     # Envia atualizações periódicas e verifica timeouts
    ├── ThreadDeRecepcao.java      # Recebe e processa mensagens UDP
    └── ThreadDoTerminal.java      # Interface de linha de comando
```

## Dependências

- **Java Development Kit (JDK) 8 ou superior**
- Sistema Operacional: Windows (o script .bat é específico para Windows)

Não há dependências externas. O projeto utiliza apenas bibliotecas padrão do Java:
- `java.net.*` - Para comunicação via sockets UDP
- `java.io.*` - Para entrada/saída de dados
- `java.time.*` - Para gerenciamento de timestamps
- `java.nio.file.*` - Para leitura de arquivos

## Como Executar

### Método 1: Usando o script .bat (Recomendado)

1. Certifique-se de ter o JDK instalado e configurado no PATH
2. Edite o arquivo `roteadores.txt` e adicione os IPs dos roteadores vizinhos (um por linha)
3. Execute o arquivo `executar.bat`:
   ```
   executar.bat
   ```

O script irá:
- Compilar todos os arquivos Java
- Abrir automaticamente um terminal para logs
- Iniciar o roteador no terminal de comandos

### Método 2: Compilação e Execução Manual

1. Compile os arquivos:
   ```cmd
   javac -encoding UTF-8 Main.java TerminalDeLogs.java Entidades/*.java Threads/*.java
   ```

2. Execute a aplicação:
   ```cmd
   java Main
   ```

## Configuração

### Arquivo `roteadores.txt`

Este arquivo deve conter os endereços IP dos roteadores vizinhos, um por linha:

```
192.168.1.10
192.168.1.11
192.168.1.12
```

**Importante**: O roteador ignora automaticamente seu próprio IP da lista.

### Portas Utilizadas

- **Porta 6000**: Comunicação UDP entre roteadores
- **Porta 7000**: Comunicação TCP para o terminal de logs

## Comandos Disponíveis

No terminal de comandos, você pode usar:

- `enviar <ip> <mensagem>` - Envia uma mensagem de texto para o IP de destino
- `vizinhos` - Exibe a tabela de vizinhos ativos
- `rotas` - Exibe a tabela de roteamento completa
- `ler` - Recarrega a lista de vizinhos do arquivo `roteadores.txt`
- `sair` - Encerra o roteador

### Exemplos de Uso

```
> vizinhos
Tabela de Vizinhos:
- 192.168.1.10
- 192.168.1.11

> rotas
Tabela de Roteamento:
- Destino: 192.168.1.10, Métrica: 1, Próximo Salto: 192.168.1.10
- Destino: 192.168.1.11, Métrica: 1, Próximo Salto: 192.168.1.11
- Destino: 192.168.1.12, Métrica: 2, Próximo Salto: 192.168.1.10

> enviar 192.168.1.12 Olá, mundo!
```

## Protocolo de Comunicação

O sistema utiliza mensagens UDP com prefixos para identificar o tipo:

- `@<IP>` - Mensagem de anúncio (descoberta de vizinho)
- `*<IP>;<métrica>*<IP>;<métrica>...` - Atualização de tabela de roteamento
- `!<IP_origem>;<IP_destino>;<texto>` - Mensagem de texto

## Funcionamento

1. **Inicialização**: O roteador lê o arquivo de vizinhos e envia anúncios
2. **Heartbeat**: A cada 10 segundos, envia atualizações da tabela de roteamento
3. **Recepção**: Thread dedicada processa mensagens recebidas
4. **Timeout**: Vizinhos inativos por mais de 15 segundos são removidos
5. **Atualização de Rotas**: Implementa o algoritmo Bellman-Ford distribuído

## Observações

- O sistema detecta automaticamente o IP local da máquina
- Logs são exibidos em terminal separado para não interferir na entrada de comandos
- A métrica representa o número de saltos até o destino
- Rotas são atualizadas dinamicamente conforme mudanças na topologia da rede

## Autores

Projeto desenvolvido como Trabalho Final da disciplina de Fundamentos de Redes de Computadores.

## Licença

Este projeto é de uso educacional.
