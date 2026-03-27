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
