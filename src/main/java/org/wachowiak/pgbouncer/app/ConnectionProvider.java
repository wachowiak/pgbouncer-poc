package org.wachowiak.pgbouncer.app;

import java.sql.Connection;
import java.sql.SQLException;

interface ConnectionProvider {

    Connection getConnection() throws SQLException;
}
