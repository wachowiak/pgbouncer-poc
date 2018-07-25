package org.wachowiak.pgbouncer.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RawConnectionProvider implements ConnectionProvider{

    private final DbProperties dbProperties;

    public RawConnectionProvider(String uri, String userName, String password) {
        dbProperties = new DbProperties(uri, userName, password);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbProperties.getUrl(), dbProperties.getProperties());
    }
}
