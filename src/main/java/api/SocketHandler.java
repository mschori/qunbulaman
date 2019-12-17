package api;

import java.io.*;
import java.net.Socket;

public class SocketHandler extends Thread {
    private BufferedReader inStream = null;
    private PrintWriter outStream = null;
    private Socket socket = null;

    public SocketHandler(Socket sock) {
        try {
            inStream = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            outStream = new PrintWriter(sock.getOutputStream(), true);
            socket = sock;
        } catch (IOException e) {
            System.out.println("Couldn’t initialize SocketAction:" + e);
            System.exit(1);
        }
    }

    public void send(String message) {
        outStream.println(message);
    }

    public String receiveForce() throws IOException {
            return inStream.readLine();
    }

    public String receive() throws IOException {
        if (inStream.ready()){
            return inStream.readLine();
        }else{
            return "empty";
        }
    }

    public void closeConnections() {
        try {
            socket.close();
            socket = null;
        }
        catch (IOException e) {
            System.out.println("Couldn’t close socket:" + e);
        }
    }

    public boolean isConnected() {
        return ((inStream != null) && (outStream != null) && (socket != null));
    }

}
