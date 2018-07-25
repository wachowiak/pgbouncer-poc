# pgbouncer-demo


An example showing how to communicate with a database with a help of connection pools, especially PgBouncer and HikariDB

This repository was created for purpose of article: __PgBouncer: connection pooling for PostgreSql__, which can be read here: http://www.wachowiak.org/2018/07/pgbouncer-connection-pooling-for.html

Usage:

```gradle run  -PappArgs="['-n']"``` to run application __without__ connection pool

```gradle run  -PappArgs="['-l']"``` to run application with __HikariDB__ connection pool enabled

```gradle run  -PappArgs="['-r']"``` to run application with __PgBouncer__ connection pool enabled