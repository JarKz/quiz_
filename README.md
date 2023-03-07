# quiz_
Application that based on java Spring Boot

## About pet-project
Using this application, you can walkthrough any created quizzes. After it action you can check the results about walkthrough. If you wanna create your quizzes then grow up role to "creator" in personal page. Then you can visit quiz management page where you can make quizzes and his questions with any types: TEXT, CHECKBOX, RADIO.

## Requirement

- JDK 19
- PostgreSQL
- Gradle 7.6

## Preparation

Install postgres application in your machine. Then make specific directory for store data. For examle, we'll use directory "quiz_db":
```bash
initdb quiz_db
```

Use it for create database "quiz". Run postgres using command:
```bash
pg_ctl -D quiz_db start
```

And connect to recently runned server:
```bash
psql -U {your username or "postgres"}
```

In opened application in terminal create database using SQL query:
```SQL
CREATE DATABASE quiz;
```

In current moment you can change user or password if you want do it.
(Not necessary) You can make script file for creating env variables:
```bash
touch make_env_variables.sh
```

Open this file using vi or nano and insert:
```sh
export DB_USER={your db user}
export DB_PASSWORD={your password for db user}
```

After previous steps clone this git pet-project in another directory. It's all.

## Usage

Before build jar file, need run postgres server if you don't run server. Then in other terminal window run postgres using command:
```bash
pg_ctl -D {path_to}/quiz_db start
```
Or
```bash
postgres -D {path_to}/quiz_db
```

Postgres server shall run on standard port: 5432.
Second step – export the environment variables DB_USER and DB_PASSWORD in terminal window where you will run application. You can run make_evn_variables.sh if you did create it or run in terminal:
```bash
export DB_USER={your db user}
export DB_PASSWORD={your password for db user}
```

Third step – run gradle command:
```bash
gradle build
```

If you don't see errors then all is ok. Else check database in you postgres server, check environment variables, check permissions for your database user. After generating jar file move to directory "build/libs". There shall are "backend-1.0-RELEASE.jar". Run it using command:
```bash
java -jar backend-1.0-RELEASE.jar
```

And enjoy it!

## Afterword

This pet-project are maked for better understanding the Spring framework. Therefore I did focus logic in controllers, designed relationships, maked security, communication between server and client (in other words, injection models to template), "rendering" with javascript code in templates, making the tests. And on this pet-project any people can make his application if they don't understand how to do it.

Please understand it.

## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://github.com/JarKz/quiz_/blob/main/LICENSE)
