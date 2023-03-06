Approach:

- I first spent some time to analyze the responses from the endpoints provided. After going over the task requirements thoroughly, I thought about the data structure that I would need. And things became clearer.
- For the data structure, there were many options of course, but I decided to go ahead with the minimum required. So I went ahead with
  teamMemberRoleData: {teamId: {userId: role}}
  as this will be enough for me to keep exactly what I need.
- With the implementation, I tried to keep the logic simple while filling as many requirements from extra points as possible. 
- I found out that I didn't have to use Users API to handle the requirements in this task. Thus I didn't call that API.
- For this implementation, I needed to call /teams/ endpoint once but also /teams/{teamId} endpoint for as many times as there are response items from the /teams/ endpoint. With a slight addition on Teams API, it would be much more convenient and efficient. Thus follows my request.


Request to Teams API team devs:

- Please provide the ability to return all teamIds alongside with their members in 1 call, like the /teams/ endpoint. Just a response in this format would be perfect:
  [team1Id: [teamMemberId1, teamMemberId2, ...], team2Id: [teamMemberId1, teamMemberId2, ...], ...]


Architecture:

- Used Spring to automatically handle scalability, dependency injection and Rest API functionalities. This simple application wouldn't necessarily require Spring, but it is more structured and easier to maintain with Spring. 
- Used Gradle for easy packaging and dependency management of the project
- Used RestTemplate for the requests, but in the future the service could be refactored to use WebClient for async calls to Teams API and potentially other APIs for scaling. However, this is not needed at this point as it also would increase code complexity.
- For testing, I decided to use Spock framework instead of Kotlin. The main reason for that is readability. Spock provides the ability to understand the system behaviour in an easier way just from reading the tests. As it uses a descriptive, close to natural language syntax that is easier to understand. 


Future Plans:
- Building a React app that uses the APIs implemented, instead of the Swagger-UI that is configured now


Running the Application

- To run via gradle without docker: (for macOS)  
./gradlew clean build  
./gradlew run  


- To run via docker:  
docker build -t teams .  
docker run -p 8080:8080 teams  


- To run via docker-compose:  
docker-compose up


- Then you can call the endpoints from  
http://localhost:8080/team-roles/
- or via swagger ui:  
http://localhost:8080/swagger-ui/index.html#/

- To run tests only: (for macOS)  
./gradlew test