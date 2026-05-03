FROM eclipse-temurin:17-jre-focal AS java

WORKDIR /app
COPY target/cloud-api-controller-web-*.jar web.jar
CMD ["java", "-jar", "web.jar"]
HEALTHCHECK --interval=30s --timeout=5s --start-period=3s --retries=3 CMD sh -c "wget --no-verbose --tries=1 --spider http://localhost:8080/health/status || exit 1"
EXPOSE 8080
EXPOSE 8081