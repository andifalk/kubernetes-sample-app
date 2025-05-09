# Kubernetes Sample Application

This basic application is a demonstration of a Spring Boot application that can be deployed to Kubernetes. It includes a RESTful API with several endpoints.

## API Endpoints

| Endpoint         | Description                                                                                                                 |
|------------------|-----------------------------------------------------------------------------------------------------------------------------|
| /api/hello       | Returns a greeting message                                                                                                  |
| /api/config      | Returns config values as a proof of correct configuration of Kubernetes ConfigMap and Secret                                |
| /actuator/health | Returns health info including readiness and liveness info for Kubernetes                                                    |
| /actuator/env    | Returns all resolved environment variables to check for missing ones (Note: Do not make this public on productive systems!) |

For convenience, you find a preconfigured IntelliJ http client in the `requests` subdirectory.
To target either the local application or the Kubernetes deployment, you can use the `dev` or `k8s` environment.

## Build and Run

To build the application, use the following command:

```bash
./mvnw clean package
```

To run the application locally, use:

```bash
./mvnw spring-boot:run
```

## Create (Docker) Container Image

To run the application in a Docker container or deploy it on Kubernetes, you first need to create a container image:

### Using Spring Boot and Buildpacks

```bash
./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=kubernetes-sample-application -Ddocker.publish=false
```

### Using Dockerfile

```bash
docker build -f docker/secure/Dockerfile -t kubernetes-sample-app .
```

The previous command builds the container image for the local platform (e.g., `amd64`). If you want to build for a different platform, you can use Docker Buildx. First, ensure that Docker Buildx is installed and set up.

To build for another platform (e.g., ARM), you can use the following command:

```bash
 docker buildx build --platform linux/arm64  -f docker/secure/Dockerfile -t kubernetes-sample-app .
```

### Using Google JIB and Distroless base image

To build the image using JIB, you don't need to have Docker installed and running. You can build the image with the following command:

```bash
./mvnw clean package jib:build -Dimage=kubernetes-sample-app
```

If you have Docker installed and running, you can build the image with JIB and publish it to a Docker registry, you can use the following command:

```bash
./mvnw clean package jib:dockerBuild -Dimage=kubernetes-sample-app
```

### Run the Container using Docker

```bash
docker run -p 8080:8080 kubernetes-sample-app
```

### Deploy to Kubernetes using ConfigMap and Secrets

The sample application requires 2 properties and uses a ConfigMap and a Secret to provide configuration values. The ConfigMap is used for non-sensitive data, while the Secret is used for sensitive data:

- `app.myconfig` (ConfigMap): This is a non-sensitive configuration value.
- `app.mysecret` (Secret): This is a sensitive configuration value.

The following command creates a ConfigMap and a Secret in the `default` namespace:

```bash
kubectl create configmap app --from-literal=app.myconfig=k8sconfig
kubectl create secret generic app --from-literal=app.mysecret=k8secret
```

Now let's deploy the application to Kubernetes.
Create a Kubernetes deployment and service using the provided YAML file:

```bash
kubectl apply -f kubernetes/deployment.yaml
```

### Expose the service to access the application:

```bash
kubectl expose deployment sample-application --type=NodePort --port 8080  --name=kubernetes-sample-app
```

Check the node port of the application using the following command:

```bash
 kubectl get svc
 
 NAME                    TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)          AGE
kubernetes              ClusterIP   10.96.0.1       <none>        443/TCP          178d
kubernetes-sample-app   NodePort    10.108.96.118   <none>        8080:32280/TCP   6s
```

In this case the node port is `32280`. You can access the application using the following URL:

```bash
http://<node-ip>:32280/api/hello
```

### Access the deployed application locally:

For development purposes or if you want to access the application locally without exposing it to the outside world, you can use `kubectl port-forward`:

```bash
kubectl port-forward deployment/sample-application 9999:8080
```

You can now access the application at `http://localhost:9999/api/hello`.

## Docker Images

You can pull the pre-built Docker images (amd64 and arm64 os arch) from the following repository:

https://hub.docker.com/repository/docker/andifalk/kubernetes-sample-app

## Reference Documentation

For further reference, please consider the following sections:

* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.4/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.4.4/maven-plugin/build-image.html)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/3.4.4/reference/actuator/index.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.4.4/reference/web/servlet.html)
