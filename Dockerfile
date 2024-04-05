FROM openjdk:17
WORKDIR /app
COPY out/artifacts/discord_bot_java_jar/logos-bot-spring.jar /app/logos-bot-spring.jar
ENTRYPOINT ["java", "-jar", "logos-bot-spring.jar"]