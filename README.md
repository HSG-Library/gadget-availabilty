# Gadget-Availability (Kleinmaterialien)
Successor of https://github.com/kathrin77/availabilityService

## How to build
In the project root directory run
```
./mvnw package
```
The runnable jar will be created under `target/` 

## How to run
Java, version >=11, needs to be installed, run
```
java -jar gadget-availabilty-1.0.0.jar -DapiKey=<key> -Dmicronaut.environments=prod
```
### JVM Parameters
| Parameter | Value        |
| --------- | ------------ |
| apiKey    | Alma API key. Needs at least read access to the 'Bibs' area in the production environment|
|micronaut.environments | Currently there are two environments `local` and `prod`. The only difference is the server port: `local` uses `8080` while `prod` uses `80`.


