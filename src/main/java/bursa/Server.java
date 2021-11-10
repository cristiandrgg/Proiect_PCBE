package bursa;

import threads.BuyerThread;
import threads.SellerThread;

/**
 * Created by: Doru
 * Date: 09/11/2021
 */

public class Server {

    public static void main(String[] args) {
        System.out.println("This is a server");
        SellerThread sellerThread = new SellerThread();
        sellerThread.start();
        BuyerThread buyerThread = new BuyerThread();
        buyerThread.start();
    }
}
