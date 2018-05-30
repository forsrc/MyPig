mvn install -DskipTests

cd microservice/springboot-eureka-server
mvn dockerfile:build
mvn dockerfile:push
cd ../../

cd microservice/springboot-admin
mvn dockerfile:build
mvn dockerfile:push
cd ../../

cd microservice/springboot-zipkin-server
mvn dockerfile:build
mvn dockerfile:push
cd ../../

cd microservice/springboot-config-server
mvn dockerfile:build
mvn dockerfile:push
cd ../../

cd microservice/springboot-sso-server
mvn dockerfile:build
mvn dockerfile:push
cd ../../

cd microservice/springboot-api-gateway
mvn dockerfile:build
mvn dockerfile:push
cd ../../

cd microservice/tcc
mvn dockerfile:build
mvn dockerfile:push
cd ../../
