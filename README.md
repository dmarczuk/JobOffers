# JobOffers Application

## About project
Web application, which is created in Spring Boot. Users can display offers,
which are fetching by scheduler. Also authorized users by token, can add their new offers.
The main functionality of this project is collecting offers, from various sources, in one place.
Full version of project will be available on AWS soon.


## Main responsibilities application
<ul>
<li>Registration new users</li>
<li>Viewing offers</li>
<li>Authorisation by token</li>
<li>Adding new offers by authorized users</li>
<li>Fetching offers from server using Scheduler</li>
</ul>

## Endpoints
<img src="Endpoints.jpg">


## Technologies used in project

Code: <br>
![Static Badge](https://img.shields.io/badge/java_17-orange?style=for-the-badge&logo=openjdk&logoColor=white)  
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)  
![Maven](https://img.shields.io/badge/maven-%23DD0031.svg?style=for-the-badge&logo=apachemaven&logoColor=white)  
![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)  
![MongoDB](https://img.shields.io/badge/MongoDB-46BA3B.svg?style=for-the-badge&logo=mongodb&logoColor=white)  
![Docker](https://img.shields.io/badge/Docker-3B98BA.svg?style=for-the-badge&logo=docker&logoColor=white)  
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)  


Test: <br>
![Test](https://img.shields.io/badge/Junit5-94BA3B?style=for-the-badge&logo=Junit5&logoColor=white)  
![Test](https://img.shields.io/badge/MockMvc-CCAE27?style=for-the-badge&logo=mockmvc&logoColor=white)  
![Test](https://img.shields.io/badge/WireMock-159D4B?style=for-the-badge&logo=wiremock&logoColor=white)  
![Test](https://img.shields.io/badge/TestContainers-2ADEA7?style=for-the-badge&logo=testcontainers&logoColor=white)  
![Test](https://img.shields.io/badge/AssertJ-DE2A88?style=for-the-badge&logo=assertj&logoColor=white)  
![Test](https://img.shields.io/badge/Mockito-1FC72F?style=for-the-badge&logo=mockito&logoColor=white)
<br>

## Installation and run

#### Requirements:
<li>Docker</li>


#### To run this project:
<li>Just run following command and wait for containers to be pulled up and started. </li>  

```
docker compose up
```

<li>Alternatively you can run docker-compose file through your IDE</li>

<br>

After everything build and ready, you can start application and test using Swagger or Postman

## In future
There are some plans to do in the future:  
<li>implement simple frontend</li> 
<li>deploying application on AWS</li>
<li>add new roles like ADMIN who would be able to remove offers or users</li>
<li>add other websites from which user can fetch new offers</li>
