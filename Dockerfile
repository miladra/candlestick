FROM openjdk:11
RUN mkdir /app
ADD wait-for-it.sh /app
ADD start.sh /app
ADD target/candlestick-1.0-SNAPSHOT.jar /app
ADD partner-service-1.0.1-all.jar  /app
WORKDIR /app
RUN chmod +x wait-for-it.sh
RUN chmod +x start.sh
ENTRYPOINT ["sh", "-c", "./start.sh"]
EXPOSE 9000