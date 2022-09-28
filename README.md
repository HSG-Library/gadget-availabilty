# Gadget-Availability (Kleinmaterialien)
Successor of https://github.com/kathrin77/availabilityService

## How to build
In the project root directory run
```
./mvnw package
```
The runnable jar will be created under `target/`

## How to deploy
### Github Workflows
There are two Github workflows in place:
* On push to `develop`, which builds and deploys the app to the Microsoft Azure DEV environment (https://kleinmaterialien.azurewebsites.net)
* On push to `main` which builds and deploys the app to the Microsoft Azure PROD environment (https://gadgets.unisg.ch)

## How to run
Java, version >=11, needs to be installed.
```
java -jar gadget-availabilty-1.0.0.jar -Dalma-api.apiKey=<key> -DHMAC.secret=<secret> -Dmicronaut.environments=prod -Dadmin.username=<username> -Dadmin.password=<password>
```
### JVM Parameters
| Parameter              | Value                                                                                                                                        |
| ---------------------- | -------------------------------------------------------------------------------------------------------------------------------------------- |
| alma-api.apiKey        | Alma API key. Needs at least read access to the 'Bibs' area in the production environment                                                    |
| HMAC.secret            | Shared secret defined in the Alma Webhook Integration config                                                                                 |
| admin.username         | Username for the basic-auth login to manually reload all gadgets                                                                             |
| admin.password         | Password for the basic-auth login to manually reload all gadgets                                                                             |
| micronaut.environments | Currently there are two environments `local` and `prod`. The only difference is the server port: `local` uses `8080` while `prod` uses `80`. |

#### VSCode launch.json
Add the following to `launch.json` to run the application in VSCode:
```json
{
	"type": "java",
	"name": "Launch Application",
	"request": "launch",
	"mainClass": "ch.unisg.biblio.systemlibrarian.Application",
	"projectName": "gadget-availability",
	"vmArgs": "-Dalma-api.apiKey=<add an actual api key> -DHMAC.secret=1234 -Dmicronaut.environments=local -Dadmin.username=admin -Dadmin.password=password"
}
```
