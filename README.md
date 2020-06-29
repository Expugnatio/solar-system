# solar-system
A weather calculation program for a complex solar system

The Job Cron can be modified in src/main/resources/application.yml in the following property:
```yaml
time:
  job:
    cron-expression: '0 01 0 ? * *' #This is the default
```
To view planets position in a specific day I've managed to setup the following math graphs:

https://www.desmos.com/calculator/6iptuvovf1

The only thing yo have to do is to modify the "g" constant
______________

The Application has 3 endpoints with base url: "localhost:8080"

1. GET /system/weather?day=XX #Gets records from the 'DB' (previously populated by the Job), if job has not been executed it calculates the record and saves it

2. GET /system/weather/job #Simulates the Job Execution

3. GET /system/weather/period?days=XX #Gets a report for a period of time from '0' to 'days' 
