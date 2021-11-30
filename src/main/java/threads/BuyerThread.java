package threads;

import processes.BuyerProcess;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BuyerThread extends Thread {

    Integer i;

    public BuyerThread(Integer i) {
        this.i = i;
    }

    public void run() {
        Socket socket;
        try {

            ServerSocket Server = new ServerSocket(5056);
            while (true) {

                socket = Server.accept();
                new BuyerProcess(socket, i).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}