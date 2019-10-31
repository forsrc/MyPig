 
 
keytool -genkeypair -keyalg RSA -keysize 2048 -alias mypig-api-gateway -dname "CN=mypig-api-gateway,OU=forsrc,O=forsrc,C=CN" -ext "san=DNS:mypig-api-gateway" -validity 3650 -keystore mypig-api-gateway.jks -storepass forsrc -keypass forsrc -deststoretype pkcs12
keytool -alias mypig-api-gateway -exportcert -keystore mypig-api-gateway.jks -file mypig-api-gateway.cer -storepass forsrc
keytool -import -alias mypig-api-gateway -keystore %JAVA_HOME%\jre\lib\security\cacerts -file mypig-api-gateway.cer -trustcacerts -storepass changeit
rem keytool -import -alias mypig-api-gateway -keystore "C:\Program Files\Java\jdk1.8.0_181\jre\lib\security\cacerts" -file mypig-api-gateway.cer -trustcacerts -storepass changeit
rem keytool -delete -alias mypig-api-gateway -keystore %JAVA_HOME%\jre\lib\security\cacerts -file mypig-api-gateway.cer -storepass changeit