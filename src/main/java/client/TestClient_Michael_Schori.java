package client;

import api.Player;
import api.SocketHandler;
import api.data.Data;
import api.data.DataInteger;
import api.data.DataString;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.Socket;

public class TestClient_Michael_Schori {

    private Player player;

    public static void main(String[] args) {

        TestClient_Michael_Schori client = new TestClient_Michael_Schori();

        // Frame
        JFrame frame = new JFrame("Qunbulaman-Client");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Field-Panel
        JPanel fieldPanel = new JPanel();
        fieldPanel.setPreferredSize(new Dimension(500, 500));
        fieldPanel.setBackground(Color.white);

        // Text-Panel
        JPanel textPanel = new JPanel();
        textPanel.setPreferredSize(new Dimension(500, 200));
        textPanel.setBackground(Color.gray);

        // Textfield
        JTextArea textArea = new JTextArea();
        textArea.setPreferredSize(new Dimension(500, 200));
        textArea.setEditable(false);

        // Button-Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(500, 100));
        buttonPanel.setBackground(Color.yellow);

        // Buttons
        JButton buttonLogin = new JButton("Anmelden");
        buttonLogin.addActionListener(actionEvent -> {
            try {
                client.login("localhost", 3141);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        JButton buttonReady = new JButton("Send Ready");
        buttonReady.addActionListener(actionEvent -> {
            DataString dataString = new DataString();
            dataString.setMessage("Ready!");
            client.player.update(1, dataString);
        });

        // Key Listeners
        fieldPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyPressed(e);

                if (e.getKeyCode() == KeyEvent.VK_LEFT){
                    DataString dataString = new DataString();
                    dataString.setMessage("left");
                    client.player.update(1, dataString);
                }
                if (e.getKeyCode() == KeyEvent.VK_UP){
                    DataString dataString = new DataString();
                    dataString.setMessage("up");
                    client.player.update(1, dataString);
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT){
                    DataString dataString = new DataString();
                    dataString.setMessage("right");
                    client.player.update(1, dataString);
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN){
                    DataString dataString = new DataString();
                    dataString.setMessage("down");
                    client.player.update(1, dataString);
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE){
                    DataString dataString = new DataString();
                    dataString.setMessage("bomb");
                    client.player.update(1, dataString);
                }            }
        });

        // Adding
        frame.add(fieldPanel, BorderLayout.PAGE_START);
        frame.add(textPanel, BorderLayout.CENTER);
        textPanel.add(textArea);
        frame.add(buttonPanel, BorderLayout.PAGE_END);
        buttonPanel.add(buttonLogin);
        buttonPanel.add(buttonReady);

        // Show
        frame.pack();
        frame.setVisible(true);

        while (true) {
            try {
                fieldPanel.requestFocus();
                Data response = client.getMessage();
                client.displayResponse(response, textArea, fieldPanel);
            } catch (Exception ignored) {

            }
        }
    }

    private void login(String ip, int port) throws IOException {
        Socket socket = new Socket(ip, port);
        SocketHandler socketHandler = new SocketHandler(socket);
        this.player = new Player(socketHandler);
        DataString dataString = new DataString();
        dataString.setMessage("Name: Test-Lurch");
        this.player.update(1, dataString);
    }

    private Data getMessage() {
        return this.player.getInput();
    }

    private void displayResponse(Data response, JTextArea textArea, JPanel fieldPanel) {
        if (response instanceof DataString) {
            this.addToTextArea(textArea, ((DataString) response).getData());
        } else if (response instanceof DataInteger) {
            fieldPanel.removeAll();
            Integer[][] field = ((DataInteger) response).getData();
            for (Integer[] line : field) {
                for (Integer column : line) {
                    JPanel piece = new JPanel();
                    piece.setPreferredSize(new Dimension(45, 45));
                    if (column.equals(50)) {
                        piece.setBackground(Color.white);
                        fieldPanel.add(piece);
                    } else if (column.equals(51)) {
                        piece.setBackground(Color.gray);
                        fieldPanel.add(piece);
                    } else if (column.equals(52)) {
                        piece.setBackground(Color.black);
                        fieldPanel.add(piece);
                    } else if (column.equals(53)) {
                        piece.setBackground(Color.red);
                        fieldPanel.add(piece);
                    } else if (column.equals(54)) {
                        piece.setBackground(Color.pink);
                        fieldPanel.add(piece);
                    } else if (column.equals(61)) {
                        piece.setBackground(Color.blue);
                        JLabel number = new JLabel("1");
                        number.setFont(new Font("Verdana", 1, 25));
                        number.setForeground(Color.white);
                        piece.add(number);
                        fieldPanel.add(piece);
                    } else if (column.equals(62)) {
                        piece.setBackground(Color.blue);
                        JLabel number = new JLabel("2");
                        number.setFont(new Font("Verdana", 1, 25));
                        number.setForeground(Color.white);
                        piece.add(number);
                        fieldPanel.add(piece);
                    } else if (column.equals(63)) {
                        piece.setBackground(Color.blue);
                        JLabel number = new JLabel("3");
                        number.setFont(new Font("Verdana", 1, 25));
                        number.setForeground(Color.white);
                        piece.add(number);
                        fieldPanel.add(piece);
                    } else if (column.equals(64)) {
                        piece.setBackground(Color.blue);
                        JLabel number = new JLabel("4");
                        number.setFont(new Font("Verdana", 1, 25));
                        number.setForeground(Color.white);
                        piece.add(number);
                        fieldPanel.add(piece);
                    }
                }
            }
            fieldPanel.revalidate();
        }
    }

    private void addToTextArea(JTextArea textArea, String message) {
        textArea.append("\n" + message);
    }

}
