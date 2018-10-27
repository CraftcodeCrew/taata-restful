# taata
REST-Service

Base URL: http://localhost:8080/api

Category resource:

| URI              | GET                | POST               | PUT               | DELETE            |
|------------------|--------------------|--------------------|-------------------|-------------------|
| /categories      | List of categories | Create a category  | Not allowed       | Not allowed       |
| /categories/{id} | A category         | Not allowed        | Update a category | Delete a category |

Insurable resource:

| URI              | GET                | POST               | PUT                | DELETE             |
|------------------|--------------------|--------------------|--------------------|--------------------|
| /insurables      | List of insurables | Create a insurable | Not allowed        | Not allowed        |
| /insurables/{id} | A insurable        | Not allowed        | Update a insurable | Delete a insurable |

Insurance resource:

| URI              | GET                | POST               | PUT                | DELETE             |
|------------------|--------------------|--------------------|--------------------|--------------------|
| /insurances      | List of insurances | Create a insurance | Not allowed        | Not allowed        |
| /insurances/{id} | A insurance        | Not allowed        | Update a insurance | Delete a insurance |
