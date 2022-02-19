**Customer Statement Processor**

Small service to validate pseudo MT940 messages

Service can be build by:
mvn clean package

Service can be run by:
mvn spring-boot:run

or if you want to run it in a docker container:

docker-compose up --build

spinning down the container can be done by:
docker-compose down

The service can be tested using the provided Swagger interface at http://localhost:9080/csp/swagger-ui/index.html


