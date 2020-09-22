package sample;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable{

    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
    private int nPort;

    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos){
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run() {
        String received;
        String toreturn;
        while (true)
        {
            try {
                // receive the answer from client
                received = dis.readUTF();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
