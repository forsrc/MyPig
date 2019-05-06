
cd app

echo ====================== `hostname` start...
echo \
     java \
     $JAVA_OPTS \
     -Djava.security.egd=file:/dev/./urandom \
     -jar app.jar \
     $PARAM_OPTS
echo ====================== `hostname`

java \
     $JAVA_OPTS \
     -Djava.security.egd=file:/dev/./urandom \
     -jar app.jar \
     $PARAM_OPTS