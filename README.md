# MarketShare-para-CardGame
Projeto de Desenvolvimento de Sistemas Distribuídos, um MarketShare de cartas para um CardGame.

### Arquitetura
A arquitetura escolhida é Pub/Sub com um servidor tolerante a falhas, ou seja, para o cliente será o mesmo que interagir com um servidor centralizado, mas na prática haverão diversos servidores que trocam mensagens de atualização entre si. Os publish e subscribes serão feitos pelos clientes para anunciar interesse por uma carta em específico e para colocar à venda uma carta em específico também.
![Arquitetura-Pub/Sub](https://github.com/Giovanilucass/MarketShare-para-CardGame/blob/main/Diagrama%20Arquitetura.png)
