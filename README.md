# small-baby
An API that automatically generates listings on various e-commerce websites

## Build with Gradle 4.9

A wrapper version of Gradle 4.9 will be used to build the project. The wrapper
allows developer run the project without having to install gradle globally on their machine. This will ensure all developer are using the same version on gradle.
#### Commands
Read the gradle docs for more information
- ```gradle build``` will compile the project.
- ```gradle test``` will run you test
- ```gradle bootJar``` create a jar
- ```gradle bootRun``` will run the Spring application

## Technology Stack
#### Backend
- Spring MVC 2

#### Test Framework
- JUnit 5
- Mockito

## Version Control Instructions
- When creating a branch or making a PR/commit add the Jira ticket to the title. This generates a hyperlink inside the Jira Ticket when you push your changes
  - Commit Example: git commit -m "TEST-123 updated the README"
  - PR title: TEST-123 Updated README
  - Branch: git checkout -b TEST-123_updatingReadMe


##Dev Set Up

**Start up:**
1. Clone this repo

####Run as an app
1. ./gradlew build
2. java -jar {jar_file.jar} ```Example: java -jar build/libs/fcgl-small-baby-0.0.1-SNAPSHOT.jar``` 

Once it is up the following should work://TODO: In my personal its 8081 but on my work its 8080. 
```
curl http://localhost:8081/googoo
gaga!
```

## Tests

We use JUnit for testing. You can run //TODO: insert important commands

# Create Vendor Listing Endpoint
Small Baby Services exposes an endpoint //TODO: Insert endpoint that// that accepts POST requests 
and the following body format:
//TODO Insert JSon:








