package bursa;

import models.Buyer;
import threads.BuyerThread;
import threads.SellerThread;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by: Doru
 * Date: 09/11/2021
 */

public class Server {

    public static void main(String[] args) throws SQLException {
        SqliteDB sqliteDB = new SqliteDB();

        System.out.println("This is a server");

        //SellerThread sellerThread = new SellerThread(1);
        //sellerThread.start();
        BuyerThread buyerThread = new BuyerThread(2);
        buyerThread.start();


        //List<Buyer> result = sqliteDB.getBuyers();

       // System.out.println(result.get(0).getName());
      //  System.out.println(result.get(1).getName());
      //  System.out.println(result.get(2).getName());
    }
}
