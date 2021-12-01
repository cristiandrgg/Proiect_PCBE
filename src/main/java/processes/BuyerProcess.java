package processes;

import bursa.SqliteDB;
import models.Bid;
import models.Buyer;
import models.Stock;

import java.sql.SQLException;
import java.util.List;

public class BuyerProcess extends Thread {

    private Buyer buyer;
    SqliteDB sqliteDB = new SqliteDB();
    boolean semaphore = false;

    public BuyerProcess(Buyer buyer) {
        this.buyer = buyer;
    }

    public void run(){
        while(true){
            try {
                List<Stock> stocks = sqliteDB.getStocks();
                for (Bid bid : buyer.getBidList()) {
                    System.out.printf("Buyer with id %s placed bid with id %s%n", buyer.getId(), bid.getId());
                    for (Stock stock : stocks) {
                        if (bid.getStockId().equals(stock.getId())) {
                            if (bid.getPrice() == stock.getPrice()) {
                                // TODO
                            } else {
                                // TODO creste pret
                            }
                        }
                    }
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}