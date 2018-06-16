mvn install -DskipTests

cd microservice/springboot-eureka-server
mvn dockerfile:build -DskipTests
#mvn dockerfile:push
docker push forsrc/forsrc-mypig-microservice-springboot-eureka-server
cd ../../

cd microservice/springboot-admin
mvn dockerfile:build -DskipTests
#mvn dockerfile:push
docker push forsrc/forsrc-mypig-microservice-springboot-admin
cd ../../

cd microservice/springboot-zipkin-server
mvn dockerfile:build -DskipTests
#mvn dockerfile:push
docker push forsrc/forsrc-mypig-microservice-springboot-zipkin-server
cd ../../

cd microservice/springboot-config-server
mvn dockerfile:build -DskipTests
#mvn dockerfile:push
docker push forsrc/forsrc-mypig-microservice-springboot-config-server
cd ../../

cd microservice/springboot-sso-server
mvn dockerfile:build -DskipTests
#mvn dockerfile:push
docker push forsrc/forsrc-mypig-microservice-springboot-sso-server
cd ../../

cd microservice/springboot-api-gateway
mvn dockerfile:build -DskipTests
#mvn dockerfile:push
docker push forsrc/forsrc-mypig-microservice-springboot-api-gateway
cd ../../

cd microservice/tcc
mvn dockerfile:build -DskipTests
#mvn dockerfile:push
docker push forsrc/forsrc-mypig-microservice-tcc
cd ../../
