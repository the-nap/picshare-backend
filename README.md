<h1 align="center">Picshare</h1>

<div align="center"><img align="center" src="https://skillicons.dev/icons?i=java,spring,docker,kafka,angular,npm"/></div>

Picshare è un'applicazione per la condivisione di immagini.

L'architettura del sistema è strutturata a microservizi. L'utente può registrarsi tramite nome utente e password, essere autenticato da Keycloak, e navigare la SPA Angular sul proprio browser.

Ogni richiesta api o di immagini viene indirizzata al servizio [Gateway](https://github.com/the-nap/picshare/gateway), che si occupa dell'autorizzazione del token JWT e della reindirizzazione opportuna.

Il [Discovery-Server](https://github.com/the-nap/picshare/services/discovery-server) si occupa di mantenere riferimenti ai vari servizi.

In [Storage Spi](https://github.com/the-nap/picshare/services/keycloak/providers/keycloak-storage-spi) si può trovare un modulo che si occupa della comunicazione tra keycloak e [User-Service](https://github.com/the-nap/picshare/services/user-service), il servizio che si occupa di mantenere i dati degli utenti registrati.

La comunicazione di eventi o cambi di stato tra i vari servizi viene effettuata tramite kafka, astratto grazie [Spring Cloud Stream](https://spring.io/projects/spring-cloud-stream).

Ogni servizio è interamente contenuto all'interno del proprio container. Ogni servizio può accedere solo al relativo database, contenuto in un container a parte, all'interno dello stesso network.

I servizi non sono esposti alla rete esterna, perciò l'autorizzazione è interamente affidata al gateway. Solo per alcune richieste specifiche viene fatto un ulteriore controllo all'interno dei vari servizi, il cui scopo però è l'identificazione più che l'autorizzazione.

All'interno di ogni servizio si può trovare il relativo readme.

Per avviare l'applicazione è necessario specificare le variabili d'ambiente per ogni servizio in specifici file descritti in compose.yaml:

- POSTGRES_DB
- POSTGRES_USER
- POSTGRES_PASSWORD

- SPRING_DATASOURCE_URL
- SPRING_DATASOURCE_USERNAME
- SPRING_DATASOURCE_PASSWORD
- SPRING_DATASOURCE_DRIVER_CLASS_NAME

Per le variabili relative a Keycloak, consultare [qui](https://www.keycloak.org/guides)

Una volta configurate le variabili d'ambiente, si potrá accedere all'applicazione tramite:
```
docker compose up
```

oppure:
```
docker compose -f compose-development.yaml up --watch
```
per entrare in modalitá sviluppo


Gran parte delle soluzioni implementate sono prese da [Stack Overflow](https://stackoverflow.com) o [Medium](https://medium.com).

