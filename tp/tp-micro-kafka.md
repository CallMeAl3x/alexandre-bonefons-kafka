### TP architecture microservice Kafka

En vous basant sur le schéma et l'architecture mise en place, vous devez mettre en place le fonctionnement suivant : 

- Modifier votre Entity Account pour intégrer comme nouvelle information, en temps réel, le nombre total de Card et Loan pour un Account.
- Pour cela, on utilisera un Broker Kafka pour faire en sorte que dès que une Card est crée pour un Account dans le microservice Card, on modifie, ensuite, le nombre total de cet Card dans le microservice Account. En utilisant le Broker Kafka.
- Dès qu' un Loan est crée pour un Account dans le microservice Loan, on modifie, ensuite, le nombre total de cet Loan dans le microservice Account. En utilisant le Broker Kafka.
- Le nombre total de Card et Loan peut evoluer à la hause et à la baisse dans le microservice Account dès qu'on crée ou supprime un  Card ou Loan.