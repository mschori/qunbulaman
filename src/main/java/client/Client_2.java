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


public class Client_2 {

    public static final int HEIGHT = 750, WIDTH = 550;

    private Player player;

    public static ClientFramesPerSecond thread;

    public static void main(String[] args) {

        Client_2 client = new Client_2();


        // Frame
        JFrame frame = new JFrame("Qunbulaman");
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);


        // Panels
        JPanel panelMain = new JPanel(new FlowLayout()); // Panel over all
        JPanel panelTop = new JPanel(new FlowLayout()); // Contains the buttons, textfield and a label
        JPanel panelMid = new JPanel(new FlowLayout()); // Will be displaying the Gamefield
        JPanel panelBottom = new JPanel(new FlowLayout()); // Displays the Messages from the Actions


        // Set the ContentPane
        frame.setContentPane(panelMain);


        // Panel Settings
        panelTop.setPreferredSize(new Dimension(WIDTH, 20));
        panelMid.setPreferredSize(new Dimension(WIDTH, 450));
        panelBottom.setPreferredSize(new Dimension(WIDTH, 100));
        BoxLayout layout1 = new BoxLayout(panelMain, BoxLayout.Y_AXIS);
        panelMain.setLayout(layout1);


        // Content Settings
        // Name Label
        JLabel nameLabel = new JLabel("Name: ");


        // Input Field for your Name
        JTextField inputTextField = new JTextField(20);


        // Buttons
        JButton submit = new JButton("Anmelden");
        submit.addActionListener(actionEvent -> {
            String username = inputTextField.getText();
            try {
                client.login("localhost", 3141, "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        JButton ready = new JButton("Ready");
        ready.addActionListener(actionEvent -> {
            DataString dataString = new DataString();
            dataString.setMessage("Ready!");
            client.player.update(1, dataString);
        });


        // Textarea where the Servers Messages display
        JTextArea textArea = new JTextArea();
        textArea.setPreferredSize(new Dimension(WIDTH-20, 100));


        // Key Listeners
        panelMid.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyPressed(e);

                if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A){
                    DataString dataString = new DataString();
                    dataString.setMessage("left");
                    client.player.update(1, dataString);
                }
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W){
                    DataString dataString = new DataString();
                    dataString.setMessage("up");
                    client.player.update(1, dataString);
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D){
                    DataString dataString = new DataString();
                    dataString.setMessage("right");
                    client.player.update(1, dataString);
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S){
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


        // Adding everything together
        frame.getContentPane().add(panelTop);
        frame.getContentPane().add(panelMid);
        frame.getContentPane().add(panelBottom);
        panelTop.add(nameLabel);
        panelTop.add(inputTextField);
        panelTop.add(submit);
        panelTop.add(ready);
        panelBottom.add(textArea);


        // Show
        frame.setVisible(true);


        // Puts the Focus on the Mid-Panel,
        // differentiate between the variations of reponses and displays each response in panelMid or TextArea
        while (true) {
            try {
                panelMid.requestFocus();
                Data response = client.getMessage();
                client.displayResponse(response, textArea, panelMid);
            } catch (Exception ignored) {
                System.out.println("The Catch");
            }
        }
    }


    //Login with Ip Port and Username with Response from each Player
    private void login(String ip, int port, String username) throws IOException {
        Socket socket = new Socket(ip, port);
        SocketHandler socketHandler = new SocketHandler(socket);
        this.player = new Player(socketHandler);
        DataString dataString = new DataString();
        dataString.setMessage("Name: Daniel" + username);
        this.player.update(1, dataString);
    }


    private Data getMessage() {
        return this.player.getInput();
    }


    private void displayResponse(Data response, JTextArea textArea, JPanel panelMid) {
        if (response instanceof DataString) {
            this.addToTextArea(textArea, ((DataString) response).getData());

        } else if (response instanceof DataInteger) {
            panelMid.removeAll();
            Integer[][] field = ((DataInteger) response).getData();
            for (Integer[] line : field) {
                for (Integer column : line) {
                    JPanel piece = new JPanel();
                    piece.setPreferredSize(new Dimension(45, 45));
                    if (column.equals(50)) {
                        piece.setBackground(Color.white);
                        panelMid.add(piece);
                    } else if (column.equals(51)) {
                        piece.setBackground(Color.gray);
                        panelMid.add(piece);
                    } else if (column.equals(52)) {
                        piece.setBackground(Color.black);
                        panelMid.add(piece);
                    } else if (column.equals(53)) {
                        piece.setBackground(Color.red);
                        panelMid.add(piece);
                    } else if (column.equals(54)) {
                        piece.setBackground(Color.pink);
                        panelMid.add(piece);
                    } else if (column.equals(61)) {
                        piece.setBackground(Color.blue);
                        JLabel number = new JLabel("1");
                        number.setFont(new Font("Verdana", 1, 25));
                        number.setForeground(Color.white);
                        piece.add(number);
                        panelMid.add(piece);
                    } else if (column.equals(62)) {
                        piece.setBackground(Color.blue);
                        JLabel number = new JLabel("2");
                        number.setFont(new Font("Verdana", 1, 25));
                        number.setForeground(Color.white);
                        piece.add(number);
                        panelMid.add(piece);
                    } else if (column.equals(63)) {
                        piece.setBackground(Color.blue);
                        JLabel number = new JLabel("3");
                        number.setFont(new Font("Verdana", 1, 25));
                        number.setForeground(Color.white);
                        piece.add(number);
                        panelMid.add(piece);
                    } else if (column.equals(64)) {
                        piece.setBackground(Color.blue);
                        JLabel number = new JLabel("4");
                        number.setFont(new Font("Verdana", 1, 25));
                        number.setForeground(Color.white);
                        piece.add(number);
                        panelMid.add(piece);
                    }
                }
            }
            panelMid.revalidate();
        }
    }


    // Adding message from Server to TextArea
    private void addToTextArea(JTextArea textArea, String message) {

            textArea.append(message + "\n");

    }


}
