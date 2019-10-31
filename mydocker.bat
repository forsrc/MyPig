rem mvn clean install -DskipTests

cd microservice/springboot-eureka-server
mvn dockerfile:build -DskipTests
rem mvn dockerfile:push
rem docker push forsrc/mypig-eureka-server
cd ../../

cd microservice/springboot-admin
mvn dockerfile:build -DskipTests
rem mvn dockerfile:push
rem docker push forsrc/mypig-admin-server
cd ../../

cd microservice/springboot-zipkin-server
mvn dockerfile:build -DskipTests
rem mvn dockerfile:push
rem rem docker push forsrc/mypig-zipkin-server
cd ../../

cd microservice/springboot-config-server
mvn dockerfile:build -DskipTests
rem mvn dockerfile:push
rem docker push forsrc/mypig-config-server
cd ../../

cd microservice/springboot-sso-server
mvn dockerfile:build -DskipTests
rem mvn dockerfile:push
rem docker push forsrc/mypig-sso-server
cd ../../

cd microservice/springboot-api-gateway
mvn dockerfile:build -DskipTests
rem mvn dockerfile:push
rem docker push forsrc/mypig-api-gateway
cd ../../

cd microservice/tcc
mvn dockerfile:build -DskipTests
rem mvn dockerfile:push
rem docker push forsrc/mypig-tcc
cd ../../
