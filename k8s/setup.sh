#!/bin/sh
if [ $profile == "prd"]
then
java -javaagent:/newrelic.jar -Dspring.profiles.active=$profile -jar application.jar
else
java -Dspring.profiles.active=$profile -jar application.jar
fi