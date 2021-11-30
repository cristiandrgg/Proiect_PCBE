package threads;

import processes.SellerProcess;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SellerThread extends Thread {

    Integer i;

    public SellerThread(Integer i) {
        this.i = i;
    }

    public void run() {
        Socket socket;
        try {
            ServerSocket Server = new ServerSocket(6000);
            while (true) {
                socket = Server.accept();
                new SellerProcess(socket, i).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
