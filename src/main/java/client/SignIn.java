package client;

import javax.swing.*;

public class SignIn {

    public static final int HEIGHT = 650, WIDTH = 1000;

    public JFrame frame;
    private QunbulamanPanel panel;
    private QunbulamanThread thread;

    public static void main(String[] args) {
        new SignIn();
    }

    public SignIn() {

        frame = new JFrame("Qunbulaman");
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);

        panel = new QunbulamanPanel();
        frame.setContentPane(panel);

        thread = new QunbulamanThread(panel);
        thread.start();


    }

}
