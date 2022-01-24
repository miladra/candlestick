#!/usr/bin/env bash
echo "Checking for database status ..."
./wait-for-it.sh mysql-standalone:3306 -t 15 &
java -jar partner-service-1.0.1-all.jar --port=8032 &
java -jar candlestick-1.0-SNAPSHOT.jar