# Viewing Swagger Docs
Copy and paste the JSON files into http://editor.swagger.io


# Generating Swagger Documentation

**Step 1**

Add springfox-swagger-2 dependency in the pom.xml of all services
```xml
<dependency>
<groupId>io.springfox</groupId>
<artifactId>springfox-swagger2</artifactId>
<version>2.9.2</version>
</dependency>
```

**Step 2**

Add @EnableSwagger2 Annotation to all services
```java
@EnableSwagger2
public class AccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountApplication.class, args);
	}

}

```
**Step 3**

Change the docker-compose file to read from our Dockerfiles. This involves changing the line 
```yml
image: sqshq/piggymetrics-account-service
```
to
```yml
build: account-service
```
End Result:
```yml
  account-service:
    environment:
      CONFIG_SERVICE_PASSWORD: $CONFIG_SERVICE_PASSWORD
      ACCOUNT_SERVICE_PASSWORD: $ACCOUNT_SERVICE_PASSWORD
      MONGODB_PASSWORD: $MONGODB_PASSWORD
    build: account-service
    restart: always
    depends_on:
      config:
        condition: service_healthy
    logging:
      options:
        max-size: "10m"
        max-file: "10"
```

Repeat this for all services that you wish to read generate Swagger documentation for

**Step 4**

Build the project
```
mvn package -DskipTests
```


**Step 5**

Run the following command to spin up the services
```
docker-compose up -d
```


**Step 6**

Generate an service token by using the following command:
```
curl --location --request POST 'localhost/uaa/oauth/token' \
--header 'Authorization: Basic YWNjb3VudC1zZXJ2aWNlOnBhc3N3b3Jk' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'grant_type=client_credentials'
```

**Step 7**

Generate a GET request for the service you want to generate Swagger docs for, but append the following to
URL:

```
/v2/api-docs
```
It should look something like this:
```
http://localhost/accounts/v2/api-docs
```

**Step 8**

The following JSON response will be the generated Swagger documentation!



## Note
OpenAPI documentation for Statistics Service is unavailable as there is a 
problem spinning up Statistics Service through the Dockerfile rather than the 
uploaded image.
