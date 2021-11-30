package bursa;

import models.Buyer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqliteDB {

    Connection connection = null;
    Statement statement = null;

    public SqliteDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:bursa.db");
            System.out.println("connected");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public List<Buyer> getBuyers() throws SQLException {
        List<Buyer> list = new ArrayList<>();

        try {
            this.statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM BUYERS");

            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("nume");
                Buyer buyer = new Buyer(id, name);
                list.add(buyer);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }
}
