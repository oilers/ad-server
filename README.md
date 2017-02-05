# Ad Server

How to configure the database connection
---
1. Open `config.yml`
1. Set `database->url` to the jdbc url you wish to connect to
1. Set `database->user` and `database->password` to the user/password to connect with

How to start the Ad Server application
---

1. Run `./gradlew clean shadowJar` or `gradlew.bat clean shadowJar` to build your application
1. Start application with `java -DCLICKTIMER=<clickTimeSeconds> -jar build/libs/ad-server-1.0-SNAPSHOT-all.jar server config.yml`
1. To check that your application is running enter url `http://localhost/history`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`
