package processes;

import bursa.SqliteDB;
import models.Buyer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BuyerProcess extends Thread {

    private Socket socket = null;
    private Integer i;

    public BuyerProcess(Socket socket, Integer i) {
        this.socket = socket;
        this.i = i;
    }

    public void run() {
        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;

        try {
            bufferedReader = new BufferedReader (new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream(),true);
            printWriter.println("Enter your name: ");
            String str = bufferedReader.readLine();
            System.out.println(str);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void getBuyer() {
        SqliteDB sqliteDB = new SqliteDB();
        Statement statement = sqliteDB.getStatement();
        Connection connection = sqliteDB.getConnection();

        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(String.format("SELECT * FROM BUYERS WHERE id=%s", i));

            if(rs == null) {
                throw new RuntimeException();
            }

            while (rs.next()) {
                Integer id = rs.getInt("id");
                String name = rs.getString("nume");
                Buyer buyer = new Buyer(id, name);
                System.out.println(id);
                System.out.println(name);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}