# Trabalho Final - Fundamentos de Redes de Computadores
## Roteiro de Desenvolvimento com Entregas Incrementais

### 📋 Visão Geral do Projeto
Sistema de roteamento de mensagens em Java utilizando sockets UDP, implementando funcionalidades de descoberta de rota, encaminhamento de pacotes e gerenciamento de topologia de rede.

---

### **Entrega 1: Base de Comunicação UDP** ✅
- [x] Implementação da classe `Roteador` (main entry point)
- [x] Implementação da classe `RecebedorMensagens` (thread para recepção)
- [x] Implementação da classe `Terminal` (interface de linha de comando)
- [x] Comunicação básica UDP entre nós
- [x] Interface de linha de comando funcional

**Testes:**
- Inicialização do roteador na porta 6000
- Envio de mensagens entre instâncias locais
- Recepção e exibição de mensagens

---

### **Entrega 2: Estrutura de Dados para Roteamento**

**Tarefas:**
- [ ] Criar classe `TabelaRoteamento` para armazenar rotas conhecidas
- [ ] Implementar classe `No` para representar nós da rede
- [ ] Criar classe `Rota` para representar uma rota específica
- [ ] Implementar classe `ProtocoloMensagem` para tipos de mensagem

**Testes:**
- Criar tabela de roteamento e adicionar/remover rotas
- Verificar busca da melhor rota por destino
- Testar criação e manipulação de objetos No e Rota

---

### **Entrega 3: Protocolo de Descoberta de Vizinhos**

OBS: QUANDO LER O ARQUIVO DE CONFIGURAÇÃO MANDAR O @ COM O PRÓPRIO IP PARA SE ANUNCIAR.

**Tarefas:**
- [ ] Implementar mensagens HELLO para descoberta de vizinhos
- [ ] Criar timer para envio periódico de mensagens HELLO (a cada 10 segundos)
- [ ] Implementar detecção de vizinhos inativos (timeout de 30 segundos)
- [ ] Criar classe `GerenciadorVizinhos` para gerenciar lista de vizinhos

**Testes:**
- Iniciar 2 roteadores e verificar descoberta mútua
- Parar um roteador e verificar detecção de timeout
- Verificar envio periódico de mensagens HELLO

---

### **Entrega 4: Algoritmo de Roteamento (Distance Vector)**

**Tarefas:**
- [ ] Implementar troca de vetores de distância entre vizinhos
- [ ] Criar classe `AlgoritmoRoteamento` com algoritmo de Bellman-Ford
- [ ] Implementar atualização da tabela de roteamento
- [ ] Adicionar envio periódico da tabela para vizinhos

**Testes:**
- Configurar rede com 3 nós (A-B-C) e verificar convergência
- Testar cálculo de menor caminho
- Verificar atualização automática das rotas

---

### **Entrega 5: Encaminhamento de Pacotes**

**Tarefas:**
- [ ] Modificar protocolo para distinguir mensagens de dados e controle
- [ ] Implementar classe `EncaminhadorPacotes` para forwarding
- [ ] Adicionar TTL (Time To Live) aos pacotes de dados
- [ ] Implementar encaminhamento baseado na tabela de roteamento

**Testes:**
- Enviar mensagem de A para C através de B
- Verificar decremento de TTL a cada salto
- Testar descarte de pacotes com TTL = 0

---

### **Entrega 6: Interface de Usuário Aprimorada**

**Tarefas:**
- [ ] Adicionar comando `tabela` para exibir tabela de roteamento
- [ ] Adicionar comando `vizinhos` para listar vizinhos ativos
- [ ] Adicionar comando `stats` para estatísticas de mensagens
- [ ] Melhorar logs e mensagens informativas

**Testes:**
- Testar todos os novos comandos
- Verificar se informações exibidas estão corretas
- Validar logs durante operação da rede

---

### **Entrega 7: Tratamento de Falhas e Robustez**

**Tarefas:**
- [ ] Implementar detecção rápida de falhas de vizinhos
- [ ] Adicionar reconvergência automática após falhas
- [ ] Implementar limpeza de rotas obsoletas
- [ ] Adicionar tratamento de exceções

**Testes:**
- Parar um nó no meio da rede e verificar reconvergência
- Testar múltiplas falhas simultâneas
- Verificar limpeza de rotas antigas

---

### **Entrega 8: Otimizações e Testes Finais**

**Tarefas:**
- [ ] Otimizar performance dos algoritmos
- [ ] Implementar triggered updates para convergência rápida
- [ ] Realizar testes completos em diferentes topologias
- [ ] Documentar funcionamento final do sistema

**Testes:**
- Testar rede com múltiplos nós (até 5-6 nós)
- Verificar performance e tempo de convergência
- Teste final em topologia complexa (malha)

---

## 🛠️ Como Testar Cada Entrega

### Ambiente de Teste
```bash
# Terminal 1 - Roteador A (porta 5000)
java Roteador

# Terminal 2 - Roteador B (porta 5001)
java Roteador -porta 5001

# Terminal 3 - Roteador C (porta 5002)
java Roteador -porta 5002
```

### Testes de Integração
- **Topologia Linear:** A ↔ B ↔ C
- **Topologia Estrela:** B conectado a A e C
- **Topologia Malha:** Todos conectados a todos

### Métricas de Sucesso
- ✅ Descoberta automática de todos os nós
- ✅ Convergência em tempo hábil
- ✅ Entrega correta de mensagens
- ✅ Recuperação de falhas
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