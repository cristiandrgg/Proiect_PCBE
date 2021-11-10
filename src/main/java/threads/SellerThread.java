package threads;

import processes.SellerProcess;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SellerThread extends Thread {

    public void run() {
        Socket socket;
        try {
            ServerSocket Server = new ServerSocket(5000);
            while (true) {
                socket = Server.accept();
                new SellerProcess(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
