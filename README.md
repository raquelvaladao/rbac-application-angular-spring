# rbac-application-angular-spring
Angular, Spring, PostgreSQL application

## To run the project
Build backend file (Maven 3.8, Java 11)
```java
cd rbac-backend-spring
mvn clean install
```

Run all containers
```
cd ..
docker compose up -d
``` 

## Test
- Login with admin user type:
  - login: admin, password: admin
- Login with team user type (any user from table Users where the login is its login and the password is login_c):
  - login: <team_name_login>_c, password: team_name
- Login with pilot user type (any user from table Users where the login is its login and the password is login_d):
 - login: <pilot_name_login>_d, password: pilot_name
