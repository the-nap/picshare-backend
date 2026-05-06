<h1 align="center">Picshare Gateway</h1>

Il Gateway dell'applicazione si basa su [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway), si occupa di autorizzare e reindirizzare le richieste del frontend verso il servizio desiderato.

L'autorizzazione avviene tramite token JWT, mentre Spring Cloud Gateway, tramite i predicates ed i filters, si occupa di modificare dove necessario la richiesta ed inoltrarla verso il servizio richiesto.

Gli indirizzi dei servizi sono preceduti da 'lb', ad indicare il servizio di Load Balancing offerto da Spring Cloud Netflix.

