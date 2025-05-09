FROM bellsoft/liberica-openjre-alpine:21.0.6
ARG TARGETPLATFORM
RUN echo "I'm building for $TARGETPLATFORM"
COPY target/kubernetes-sample-app-1.0.0-SNAPSHOT.jar app.jar
EXPOSE 8080
RUN addgroup -S app -g 1002 && adduser -S appuser --u 1002 --g 1002
USER 1002
ENTRYPOINT ["java", "-jar", "/app.jar"]
