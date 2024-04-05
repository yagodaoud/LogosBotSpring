FROM openjdk:17
WORKDIR /app
COPY target/logos-bot-spring.jar /app/logos-bot-spring.jar
ENTRYPOINT ["java", "-jar", "logos-bot-spring.jar"]