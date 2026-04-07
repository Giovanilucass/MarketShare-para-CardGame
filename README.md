# MarketShare-para-CardGame
Projeto de Desenvolvimento de Sistemas Distribuídos, um MarketShare de cartas para um CardGame.

### Arquitetura
A arquitetura escolhida é Pub/Sub com um servidor tolerante a falhas, ou seja, para o cliente será o mesmo que interagir com um servidor centralizado, mas na prática haverão diversos servidores que trocam mensagens de atualização entre si. Os publish e subscribes serão feitos pelos clientes para anunciar interesse por uma carta em específico e para colocar à venda uma carta em específico também.

![Arquitetura-Pub/Sub](https://github.com/Giovanilucass/MarketShare-para-CardGame/blob/main/Diagrama%20Arquitetura.png)

### Comunicação
Qual tipo de comunicação?
- Dado a arquitetura Pub/Sub, as trocas de mensagem entre os clientes será assíncrona.
- A conexão com o servidor será persistente, mas as conexões dos clientes é temporária, na qual eles fazem o que precisam e desconectam.
- TCP pois as mensagens precisam ir com segurança, já que envolvem transações e informações descritivas concretas.

Quais os tipos de mensagem e seus formatos?
- As mensagens serão de dois tipos: Mensagens entre cliente e servidor, que serão mensagens de publish e subscribe, e mensagens entre servidores, já que ele será tolerante a falhas, as mensagens serão de atualização.
- As mensagens de atualização seriam feitas em multicast<sup>1</sup>.

1. Multicast:
	- Temos requisições realizadas pelos clientes, em que irão para o servidor principal e serão passadas entre diferentes servidores.

### Nomeação
**Quais recursos precisam ser nomeados?**
	Basicamente todos os recursos que precisam ser acessados precisam ser nomeados, isto é, o API Gateway (o canal de comunicação entre microserviços), as instâncias dos microserviços (Auth/Login, Mercado, Inventário e Recompensas, bem como suas instâncias), as cópias de bancos de dados (O atual líder e as réplicas), o servidor do Event Bus, os tópicos de mensagem no event bus (para qual microserviço será a mensagem), os usuários, as cartas e as transações.

**Qual esquema de nomeação?**
	Para o acesso aos microserviços a nomeação será de forma estruturada, isto é, ao enviar a mensagem para o Event Bus ele deverá saber qual microserviço vai ser acessado e qual função será executada naquele microserviço, além de qual instância daquele microserviço será usada. Portanto, teremos algo como: 
	**\[serviço\].\[funcao\].\[id\]**
	Já para as cartas e os usuários o esquema será plano, poderemos armazená-los todos da mesma form a e para acessá-los utilizar uma função de hash para encontrar o ID da carta ou usuário específico.

**Dado o esquema qual o mecanismo de resolução de nomes?**
	Para os serviços os nomes serão resolvidos pelo DNS interno do Docker, traduzindo o nome estruturado mostrado anteriormente para um endereço IP respectivo para o servidor do microserviço.
	O Event Bus também terá de resolver nomes, já que receberá mensagens estruturadas e também mensagens planas, ele terá que identificar essas mensagens e se comunicar com o serviço correto.

### Processos
**Faz sentido usar threads?**
	Sim, afinal, os microserviços terão que se conectar ao Event Bus, para que essa conexão possa ser feita ele terá de estar aguardando sockets em segundo plano, o mesmo acontece para os microserviços, que estarão aguardando as respostas do Event Bus o tempo todo que estiverem conectados e isso terá de ser feito em threads.

**Servidores Stateful ou Stateless?**
	Stateless, os servidores dos microserviços não guardam nenhum estado, se eles caem eles se comunicam ao Banco de Dados onde as informações estão guardadas e atualizam para o estado atual. Ou seja, todas as informações e estados (Quantas cartas de cada estão em circulação, inventário dos jogadores, usuários inscritos, etc.) estarão armazenadas no banco de dados daquele microserviço.

**Faz sentido usar técnicas de virtualização?**
	Sim, iremos utilizar docker para ser possível executar todos os banco de dados e instâncias de microserviços, já que teremos alta replicação de cada um destes, o que resultaria em aproximadamente 20 instâncias executando em paralelo.
