package org.wachowiak.pgbouncer.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);


    public static void main(String[] args) throws Exception {

        Class.forName("org.postgresql.Driver");

//        Executors.callable(Application.runPool(new DbProperties("jdbc:postgresql://localhost/pgdemo", "pgdemo", "pgdemo"))).call();
        Executors.callable(Application.runPool(new DbProperties("jdbc:postgresql://localhost/pgbouncer", "pgbouncer", "pgbouncer"))).call();
    }

    private static Runnable runPool(DbProperties dbProperties) {

        return () -> IntStream.range(1, 10000).parallel().forEach(t -> {

            long before = System.nanoTime() / 1000000;
            try (Connection conn = DriverManager.getConnection(dbProperties.getUrl(), dbProperties.getProperties())) {
                Statement statement = conn.createStatement();

                ResultSet rs = statement.executeQuery("select now()");
                while (rs.next()) {
                    LOGGER.debug(rs.getDate(1).toString());
                }
            } catch (SQLException e) {
                LOGGER.error("fail", e);
            }
            LOGGER.info("Processing time: {} ms", System.nanoTime() / 1000000 - before);
        });
    }

    private static class DbProperties{
        private String url;
        private Properties properties;

        DbProperties(String url, String user, String password) {
            this.url = url;
            properties = new Properties();
            properties.setProperty("user", user);
            properties.setProperty("password", password);
        }


        String getUrl() {
            return url;
        }

        Properties getProperties() {
            return properties;
        }
    }
}
