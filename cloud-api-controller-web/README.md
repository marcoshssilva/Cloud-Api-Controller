# Cloud-Api-Controller-Web

This is a web framework for building HTTP APIs using Java. It provides a simple and intuitive way to create RESTful APIs with minimal boilerplate code. The framework is designed to be lightweight and easy to use, making it ideal for developers who want to quickly build and deploy APIs without having to worry about the underlying infrastructure.

## Docker

To build the docker image:

```bash
mvn clean package
cd cloud-api-controller-web
docker build --tag cloud-api-controller-web:latest --file .\src\main\docker\Dockerfile --no-cache .
docker run --name cloud-api-controller-web -p 8080:8080 -p 8081:8081 cloud-api-controller-web:latest 
```