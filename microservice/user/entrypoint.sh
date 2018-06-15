
cd app

java \
     -Djava.security.egd=file:/dev/./urandom \
     -jar app.jar \
     --spring.config.location=config/application.yml \
     -classpath config/* \
     $JAVA_OPTS
