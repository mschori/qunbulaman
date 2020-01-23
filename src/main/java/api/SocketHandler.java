package api;

import api.data.Data;
import api.data.DataFactoryImpl;
import api.payload.Payload;
import api.payload.PayloadFactoryImpl;

import java.io.*;
import java.net.Socket;

public class SocketHandler extends Thread {
    private PayloadFactoryImpl payloadFactory;
    private DataFactoryImpl dataFactory;
    private BufferedReader inStream = null;
    private PrintWriter outStream = null;
    private Socket socket = null;

    public SocketHandler(Socket sock) {
        this.payloadFactory = new PayloadFactoryImpl();
        try {
            inStream = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            outStream = new PrintWriter(sock.getOutputStream(), true);
            socket = sock;
        } catch (IOException e) {
            System.out.println("Couldn’t initialize SocketAction:" + e);
            System.exit(1);
        }
    }

    public void send(Integer key, Object data) {
        Payload payload = this.payloadFactory.createPayload(key, data);
        outStream.println(payload.getPayload());
    }

    public Data receiveForce() throws IOException {
        String line = inStream.readLine();
        if (line.matches("^Message:")) {
            return this.dataFactory.createData(1, line);
        } else if (line.matches("^Field:")) {
            return this.dataFactory.createData(2, line);
        } else {
            return null;
        }
    }

    public Data receive() throws IOException {
        if (inStream.ready()) {
            String line = inStream.readLine();
            if (line.matches("^Message:")) {
                return this.dataFactory.createData(1, line);
            } else if (line.matches("^Field:")) {
                return this.dataFactory.createData(2, line);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void closeConnections() {
        try {
            socket.close();
            socket = null;
        } catch (IOException e) {
            System.out.println("Couldn’t close socket:" + e);
        }
    }

    public boolean isConnected() {
        return ((inStream != null) && (outStream != null) && (socket != null));
    }
}
