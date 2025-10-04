package sbs.immovablerod.metaDungeon.util;

import java.sql.*;

public class SQL {
    String db_path;
    Connection connection;
    Statement statement;
    ResultSet result;


    public SQL(String db_path) {
        this.db_path = db_path;
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + db_path);
            this.statement = this.connection.createStatement();
            this.statement.setQueryTimeout(30);
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    public void execute_write(String... args) {
        try {
            for (String arg : args) {
                System.out.println("(SQL) executing: " + arg);
                this.statement.executeUpdate(arg);
            }
        } catch (SQLException e) {  e.printStackTrace(System.err);}
    }

    public ResultSet execute_query(String query) {
        try {
            this.result = statement.executeQuery(query);
            return this.result;
        } catch (SQLException e) {  e.printStackTrace(System.err); return null;}
    }

    public void Close() {
        System.out.println("(SQL) closing connection @ " + this.db_path);
        if (this.result != null) {
            try {
                this.result.close();
            } catch (SQLException e) { /* Ignored */}
        }
        if (this.statement != null) {
            try {
                this.statement.close();
            } catch (SQLException e) { /* Ignored */}
        }
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) { /* Ignored */}
        }

    }

}
