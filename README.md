Approach:

- I first spent some time to analyze the responses from the endpoints provided. After going over the task requirements throughly, I thought about the data structure that I would need. Once I decided on the data structure, that made things clearer.
- For the data structure, there were many options of course, but I decided to go ahead with the minimum required. So I went ahead with
#  teamMemberRoleData: {teamId: {userId: role}}
  as this will be enough for me to keep exactly what I need.
- With the implementation, I tried to keep the logic as simple as possible while filling as many requirements from extra points as possible
- I found out that I didn't have to use Users API to handle the requirements in this task. Thus I didn't call that API.
- For this implementation, I needed to call /teams/ endpoint once but /teams/{teamId} endpoint for as many times as there are response items from the /teams/ endpoint. With a slight addition on Teams API, it would be much more convenient and efficient. Thus follows my request.


Request to Teams API team devs:

- Please provide the ability to return all teamIds alongside with their members in 1 call, like the /teams/ endpoint. Just a response in this format would be perfect:
#  [team1Id: [teamMemberId1, teamMemberId2, ...], team2Id: [teamMemberId1, teamMemberId2, ...], ...]


Architectural Decisions:

- Used Spring to automatically handle scalability, dependency injection and Rest API functionalities. This simple application wouldn't necessarily need Spring, but more structured and easier to maintain with Spring
- Used Gradle for easy packaging and dependency management of the project
- Used RestTemplate for the requests, but if needed the service could be refactored to use WebClient for async calls to Teams API and potentially others APIs for scaling. However, it is not needed at this point as it also would increase code complexity.
- For testing, I decided to use Spock framework instead of Kotlin/JUnit for a number of reasons:
a) Spock uses a more descriptive, almost natural language syntax that is easier to read and understand.
b) Spock has a built-in set of assertions, mocking and stubbing which are easier to use
c) Spock is also great for data-driven testing with parameterization, but I didn't need to use that functionality in my tests


Future Plans:
- Make validation methods to throw exceptions which are handled automatically by a custom exception handler that I would write. These exception handlers would then return the relevant Http error responses. This would not make the application any more efficient, in fact, it would probably make it a tiny bit less efficient due to the usages of exception throwing and catches. However, the TeamRoleController class would become much more readable and cleaner. Also, we would have exception handling. This would also make it easier to test the controller class.



Running the Application

--To run via gradle without docker: (for macOS)
./gradlew clean build
./gradlew run


--To run via docker:
docker build -t teams .
docker run -p 8080:8080 teams


--To run via docker-compose:
docker-compose up


--Then you can call the endpoints from http://localhost:8080/team-roles/

To run tests: (for macOS)
./gradlew test