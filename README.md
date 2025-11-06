# Trabalho Final - Fundamentos de Redes de Computadores
## Roteiro de Desenvolvimento com Entregas Incrementais

### 📋 Visão Geral do Projeto
Sistema de roteamento de mensagens em Java utilizando sockets UDP, implementando funcionalidades de descoberta de rota, encaminhamento de pacotes e gerenciamento de topologia de rede.

---

### **Entrega 1: Base de Comunicação UDP** ✅

**Funcionalidades:**
- [x] Inicialização do sistema na porta 6000
- [x] Envio de mensagens UDP para um destino específico (IP e porta)
- [x] Recepção de mensagens UDP de qualquer origem
- [x] Interface de linha de comando para interação do usuário
- [x] Comando para enviar mensagens: `enviar <ip> <porta> <mensagem>`
- [x] Comando para encerrar o sistema: `sair`

**Testes:**
- Iniciar o roteador na porta 6000
- Enviar mensagens entre instâncias locais
- Receber e exibir mensagens corretamente

---

### **Entrega 2: Estrutura de Dados para Roteamento**

**Funcionalidades:**
- [x] Armazenamento de tabela de roteamento com rotas conhecidas
- [x] Representação de vizinhos diretos com seus endereços IP
- [x] Representação de rotas individuais (destino, métrica, próximo salto)

**Testes:**
- Adicionar e remover rotas da tabela
- Buscar melhor rota para um destino específico
- Gerenciar informações de vizinhos

---

### **Entrega 3: Protocolo de Descoberta de Vizinhos**

**Observação:** Ao ler o arquivo de configuração inicial, enviar mensagem de anúncio com o próprio IP para se apresentar aos vizinhos.

**Funcionalidades:**
- [ ] Leitura de arquivo de configuração com IPs dos vizinhos iniciais
- [ ] Envio de mensagens de anúncio (HELLO) para descobrir vizinhos
- [ ] Envio periódico de mensagens HELLO a cada 10 segundos
- [ ] Detecção de vizinhos inativos após 15 segundos sem resposta
- [ ] Atualização automática da lista de vizinhos ativos
- [ ] Remoção de vizinhos que não respondem

**Testes:**
- Iniciar 2 roteadores e verificar descoberta mútua
- Desligar um roteador e verificar detecção de timeout
- Confirmar envio periódico de mensagens HELLO

---

### **Entrega 4: Algoritmo de Roteamento (Distance Vector)**

**Funcionalidades:**
- [ ] Troca de vetores de distância entre vizinhos
- [ ] Cálculo de rotas usando algoritmo de Bellman-Ford
- [ ] Atualização automática da tabela de roteamento ao receber novos vetores
- [ ] Envio periódico da tabela de roteamento para todos os vizinhos
- [ ] Cálculo do menor caminho para cada destino conhecido

**Testes:**
- Configurar rede com 3 nós (A-B-C) e verificar convergência
- Validar cálculo correto de menor caminho
- Confirmar atualização automática das rotas

---

### **Entrega 5: Encaminhamento de Pacotes**

**Funcionalidades:**
- [ ] Distinção entre mensagens de dados e mensagens de controle
- [ ] Encaminhamento de pacotes de dados baseado na tabela de roteamento
- [ ] Implementação de TTL (Time To Live) nos pacotes de dados
- [ ] Decremento de TTL a cada salto
- [ ] Descarte de pacotes com TTL = 0
- [ ] Roteamento de mensagens através de nós intermediários

**Testes:**
- Enviar mensagem de A para C através de B
- Verificar decremento correto de TTL
- Testar descarte de pacotes com TTL esgotado

---

### **Entrega 6: Interface de Usuário Aprimorada**

**Funcionalidades:**
- [ ] Comando `tabela` para exibir a tabela de roteamento atual
- [ ] Comando `vizinhos` para listar todos os vizinhos ativos
- [ ] Comando `stats` para exibir estatísticas de mensagens (enviadas/recebidas)
- [ ] Logs informativos sobre eventos da rede
- [ ] Mensagens claras sobre status das operações

**Testes:**
- Executar todos os novos comandos e verificar saídas
- Confirmar que informações exibidas estão corretas
- Validar logs durante operação normal da rede

---

### **Entrega 7: Tratamento de Falhas e Robustez**

**Funcionalidades:**
- [ ] Detecção rápida de falhas de vizinhos (timeout)
- [ ] Reconvergência automática da rede após falhas
- [ ] Recálculo de rotas quando um nó falha
- [ ] Limpeza automática de rotas obsoletas
- [ ] Tratamento de exceções e erros de rede
- [ ] Recuperação de estado após falhas temporárias

**Testes:**
- Desligar um nó no meio da rede e verificar reconvergência
- Testar múltiplas falhas simultâneas
- Verificar limpeza de rotas antigas e inválidas

---

### **Entrega 8: Otimizações e Testes Finais**

**Funcionalidades:**
- [ ] Otimização de performance dos algoritmos de roteamento
- [ ] Triggered updates para convergência mais rápida após mudanças
- [ ] Testes em diferentes topologias de rede
- [ ] Validação em redes maiores (5-6 nós)
- [ ] Documentação completa do sistema

**Testes:**
- Testar rede com múltiplos nós (até 5-6 nós)
- Medir tempo de convergência
- Teste final em topologia complexa (malha completa)
- Validar performance geral do sistema

---

## 🛠️ Ambientes de Teste

### Topologias Sugeridas
- **Topologia Linear:** A ↔ B ↔ C
- **Topologia Estrela:** B conectado a A e C (B no centro)
- **Topologia Malha:** Todos os nós conectados entre si

### Critérios de Sucesso
- ✅ Descoberta automática de todos os nós da rede
- ✅ Convergência da tabela de roteamento em tempo adequado
- ✅ Entrega correta de mensagens em qualquer topologia
- ✅ Recuperação automática após falhas de nós
- ✅ Ausência de loops

---

## 📝 Estrutura Final de Arquivos

```
TF/
├── README.md                    # Este documento
├── Roteador.java               # Classe principal ✅
├── RecebedorMensagens.java     # Thread de recepção ✅
├── Terminal.java               # Interface CLI ✅
├── TabelaRoteamento.java       # Tabela de rotas
├── No.java                     # Representação de nó
├── Rota.java                   # Estrutura de rota
├── ProtocoloMensagem.java      # Tipos de mensagem
├── GerenciadorVizinhos.java    # Descoberta de vizinhos
├── AlgoritmoRoteamento.java    # Distance Vector
├── EncaminhadorPacotes.java    # Forwarding de dados
└── docs/                       # Documentação adicional
```

---

## 🎯 Cronograma Sugerido

| Entrega | Prazo Estimado | Esforço |
|---------|---------------|---------|
| Entrega 1 | ✅ Concluída | - |
| Entrega 2 | 3 dias | 8h |
| Entrega 3 | 4 dias | 12h |
| Entrega 4 | 5 dias | 16h |
| Entrega 5 | 4 dias | 12h |
| Entrega 6 | 2 dias | 6h |
| Entrega 7 | 3 dias | 10h |
| Entrega 8 | 3 dias | 8h |

**Total Estimado:** 24 dias, 72 horas de desenvolvimento

---

## 📚 Recursos e Referências

- **RFC 1058:** Routing Information Protocol (RIP)
- **Livro:** "Computer Networks" - Tanenbaum
- **Tutorial:** Java UDP Socket Programming
- **Algoritmo:** Bellman-Ford Distance Vector

---

*Última atualização: Novembro 2025*