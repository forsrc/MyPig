keytool -genkeypair -keyalg RSA -keysize 2048 -alias mypig-sso-server -dname "CN=mypig-sso-server,OU=forsrc,O=forsrc,C=CN" -ext "san=DNS:mypig-sso-server" -validity 3650 -keystore mypig-sso-server.jks -storepass forsrc -keypass forsrc -deststoretype pkcs12
keytool -alias mypig-sso-server -exportcert -keystore mypig-sso-server.jks -file mypig-sso-server.cer -storepass forsrc
keytool -import -alias mypig-sso-server -keystore %JAVA_HOME%\jre\lib\security\cacerts -file mypig-sso-server.cer -trustcacerts -storepass changeit
rem keytool -import -alias mypig-sso-server -keystore "C:\Program Files\Java\jdk1.8.0_181\jre\lib\security\cacerts" -file mypig-sso-server.cer -trustcacerts -storepass changeit
rem keytool -delete -alias mypig-sso-server -keystore %JAVA_HOME%\jre\lib\security\cacerts -file mypig-sso-server.cer -storepass changeit
