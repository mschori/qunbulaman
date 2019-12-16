package backend;

import java.io.*;
import java.net.Socket;

public class SocketHandler extends Thread {
    private DataInputStream inStream = null;
    private PrintStream outStream = null;
    private Socket socket = null;

    public SocketHandler(Socket sock) {
        try {
            inStream = new DataInputStream(new BufferedInputStream(sock.getInputStream(), 1024));
            outStream = new PrintStream(new BufferedOutputStream(sock.getOutputStream(), 1024), true);
            socket = sock;
        } catch (IOException e) {
            System.out.println("Couldn’t initialize SocketAction:" + e);
            System.exit(1);
        }
    }

    public void send(String msg) {
        outStream.println(msg);
    }

    public String receiveTimeout() throws IOException {
            return inStream.readLine();
    }

    public String receive() throws IOException {
        if (inStream.available() > 0){
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
