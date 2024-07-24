# URL Shortener

Spring Boot based REST API that takes a URL and returns a shortened URL and uses MySQL to persist data.

# Getting Started

## Dependencies

This project depends on 
spring boot starters:
* spring-boot-starter-web: Provides web and RESTful support.
*spring-boot-starter-data-jpa: Provides JPA (Java Persistence API) support.
*spring-boot-starter-security: Provides security features.
*spring-boot-devtools: Provides additional development tools.
*spring-boot-starter-test: Provides testing support.
*Database:
postgresql: PostgreSQL JDBC driver for database connectivity.

*lombok: Provides annotations to reduce boilerplate code.

*commons-validator: Provides a set of validators for common data types.
Testing:
*spring-security-test: Provides support for testing Spring Security components.

## Project Build 

To build this project, run

```shell script
git clone https://github.com/munimahendrareddy/url-shortener.git
cd url-shortener
mvn clean install
```


**The application will be accessible on http://localhost:8080**


## API Endpoints

You can access following API endpoints at http://localhost:8080

### POST `/shorten`
It takes a JSON object in the following format as payload

```json
{
  "fullUrl":"<The URL to be shortened>"
}
```

#### cURL

```shell script
curl -X POST \
  http://localhost:8080/shorten \
  -H 'Content-Type: application/json' \
  -d '{"fullUrl":"https://example.com/example/1"}'
```

Response:

```json
{
  "shortUrl": "<shortened url for the fullUrl provided in the request payload>"
}
```

Please note that API works only with valid HTTP or HTTPS Urls. In case of malformed Url, it returns `400 Bad Request` error with response body containing a JSON object in the following format

```json
{
  "field":"fullUrl",
  "value":"<Malformed Url provided in the request>",
  "message":"<Exception message>"
}
```


#

# Url Shortening Algorithm

I thought of two approaches
1. Generating hashes for the fullUrl and storing them as key value pairs in redis cache or in postgresSQL database
2. Performing a Base62 conversion from Base10 on the id of stored fullUrl

Tested both of the approaches but in case of hashes, sometimes the hashes were longer than actual URL. Another issue was the readability and ease of remembering. So, I went with the second approach. With the Base conversion approach, even the maximum value of Long produces 10 characters which is still somewhat easy to remember. 
> There is a dependency from Google named Guava that could be used here to generate hashes. Although murmur_3_32 hash implemented in Guava was generating up to 10 characters long string, I left it for future testing and evaluation.

# Future Enhancements / Known Issues
*Since the project is for demo purposes only, passwords are in plaintext. Will consider using Jasypt to encrypt the password in the future.
*Haven't implemented Front-end application yet.
*Faced issues with auto schema generation through JPA, so delegated the schema creation to PostgreSQL container.
*Faced issues with the API not being able to get a connection while PostgreSQL was being set up. This resulted in adding specific configurations to application.properties to handle these issues. These configurations may slow down the application startup. You may adjust or remove them as needed.
*Implement HTTPS.
*Mount volumes for PostgreSQL container to persist data outside of the container.


# Contributors
email: munimahendrareddy1221149@gmail.com
