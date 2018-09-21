#! /bin/sh
if [ -z $FILE_REPO ]; then
    FILE_REPO=/repo
fi
mkdir -p $FILE_REPO
java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar