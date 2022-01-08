# BankTransactionApp
This is a toy project to build simple http server providing basic funds 'transfer' capability.

The goal of this project was to build something using simpliest tools:

- http4k for simple server
- Hikary connection pool\
- h2 as a db (can be changed but for simplicity it is there)
- plain jdbc queries for db operations


There is no Spring neither Hibernate and the result is extremely fast build/run time. Server start on m1 macbook in 1 seconds and the whole builds takes around 8 to 9 seconds.

Build 
```
./gradlew build
```
Start up
```
java -jar /build/libs/<yourJar>
```

