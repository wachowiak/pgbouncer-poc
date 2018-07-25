package org.wachowiak.pgbouncer.app;

import java.util.Properties;

class DbProperties{
    private final String USER = "user";
    private final String PASSWORD = "password";

    private final String url;
    private final Properties properties;

    DbProperties(String url, String user, String password) {
        this.url = url;
        properties = new Properties();
        properties.setProperty(USER, user);
        properties.setProperty(PASSWORD, password);
    }


    String getUrl() {
        return url;
    }

    Properties getProperties() {
        return properties;
    }
}