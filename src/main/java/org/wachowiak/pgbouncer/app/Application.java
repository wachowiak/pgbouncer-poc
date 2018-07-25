package org.wachowiak.pgbouncer.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    private static final String MODE_DISABLED = "-n";
    private static final String MODE_LOCAL_CP = "-l";
    private static final String MODE_REMOTE_CP = "-r";

    private static final String QUERY_DATE = "select now()";

    private final ConnectionProvider connectionProvider;

    Application(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public static void main(String[] args) {

        String mode = args.length == 0 ? MODE_DISABLED : args[0];

        ConnectionProvider provider;
        switch (mode) {
            case MODE_REMOTE_CP: {
                provider = new PgBouncerConnectionProvider("jdbc:postgresql://vm:6432/pgdemo", "pgdemo", "pgdemo");
                break;
            }
            case MODE_LOCAL_CP: {
                provider = new HikariConnectionProvider("jdbc:postgresql://localhost:5432/pgdemo", "pgdemo", "pgdemo");
                break;
            }
            case MODE_DISABLED:
            default: {
                provider = new RawConnectionProvider("jdbc:postgresql://localhost:5432/pgdemo", "pgdemo", "pgdemo");
                break;
            }

        }
        new Application(provider).runPool();
    }


    private void runPool() {
        int poolSize = 100;
        int callsPerClient = 10000;
        ExecutorService service = Executors.newFixedThreadPool(poolSize);
        IntStream.range(1, poolSize).parallel().forEach(i ->
                service.submit(() -> IntStream.range(1, callsPerClient).parallel().forEach(c -> askForDate()))
        );
    }

    private void askForDate() {

        long before = System.currentTimeMillis();
        Timestamp dbDate = executeSingleResultQuery(QUERY_DATE, Timestamp.class);
        LOGGER.info("Result: {}. Processing time: {} ms", dbDate, System.currentTimeMillis() - before);
    }

    private <T> T executeSingleResultQuery(String query, Class<T> clazz) {
        try (Connection conn = connectionProvider.getConnection()) {
            Statement statement = conn.createStatement();

            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                return clazz.cast(rs.getObject(1));
            }

        } catch (SQLException e) {
            LOGGER.error("query execution failed", e);
            throw new DatabaseException(e);
        }
        throw new DatabaseException("No data returned");
    }
}
