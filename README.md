
# sa-liabilities-sandpit-api

The SA Liabilities Sandpit API is a sandbox environment designed for experimentation and development related to liabilities work. 
This API serves as a testing ground for developers to create, test, and validate features related to liabilities without affecting the live environment. 
It provides a safe space to explore new ideas and workflows in the context of handling liabilities.

## Requirements

- Scala 3.3.3
- Java 11
- sbt 1.7.x


## Run Tests

Run unit tests: `sbt test`

Note: if you run into `java.lang.OutOfMemoryError` errors, add a `.sbtopts` file to the root of the project with the
following contents:

```
-J-Xmx3G
-J-XX:+UseG1GC
```