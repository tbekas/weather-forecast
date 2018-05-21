# Weather forecast app
Forecasting average temperature and pressure for next 3 days

## Prerequisites
The only requirement is to have JDK 1.8 installed with `JAVA_HOME` environment variable properly set

## Running the app
On Linux
```
./gradlew bootRun
``` 

On Windows
```
gradlew bootRun
```

## Exploring the Rest API

### Using Swagger
After successfully running the app open [Swagger UI](http://localhost:8080/swagger-ui.html).

Expand ForecastController, there is a single method `GET /data?city={name}`, try it out!

### Using CURL

Get a forecast for London
```
curl -X GET "http://localhost:8080/data?city=London" -H "accept: */*"
```
