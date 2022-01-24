# candlestick

## Used Stack 

   * Spring Boot
   * Java 11
   * Spring Framework
   * Apache-maven-3.8.4
   * RestTemplate
   * MySQL 5.7 and for test H2database
   * Swagger
   * Docker Compose
   * Lombok
   * Junit
  
# Thought Process
Please read README.md
I considered the time to start creating candlesticks from the time of the first candlestick.
I assumed the last candlestick could be updated frequently, the data of which changes based on the last quote received.
I assumed quote creation time is when it is received.
### Build Project

```
mvn clean install
```

### Run app with docker-compose

In root directory
```
mvn clean install

docker-compose up --build
```

### Run app without docker or just run .jar

If you want run application standalone please change your database configuration in application.properties

### Swagger

http://localhost:9000/swagger-ui.html

###### By: Milad Ranjbari
