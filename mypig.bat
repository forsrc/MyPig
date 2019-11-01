

java -server -Xmn128m -Xms128m -Xmx256m -jar microservice\springboot-eureka-server\target\mypig-eureka-server-0.0.1-SNAPSHOT.jar

rem set DB_URL=jdbc:h2:c:/tmp/db/h2/sso.h2 && java -server -Xmn128m -Xms128m -Xmx256m -jar microservice\springboot-sso-server\target\mypig-sso-server-0.0.1-SNAPSHOT.jar

rem set DB_URL=jdbc:h2:c:/tmp/db/h2/ui.h2 && java -server -Xmn128m -Xms128m -Xmx256m -jar ui\target\mypig-ui-0.0.1-SNAPSHOT.jar