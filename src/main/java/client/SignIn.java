package client;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;


public class SignIn extends JFrame{

    public static final int HEIGHT = 650, WIDTH = 1000;

    public JFrame frame;
    private JPanel panelMain;
    private JPanel panelTop;
    private JPanel panelMid;
    private JPanel panelBottom;
    private JLabel nameLabel;
    private JTextField inputTextField;
    private JButton submit;


    private QunbulamanThread thread;

    public static void main(String[] args) {
        new SignIn();
    }

    public SignIn() {

        frame = new JFrame("Qunbulaman");
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);

        panelMain = new JPanel(new FlowLayout());
        panelTop = new JPanel(new FlowLayout());
        panelMid = new JPanel(new FlowLayout());
        panelBottom = new JPanel(new FlowLayout());

        frame.setContentPane(panelMain);

        panelTop.setBorder(BorderFactory.createTitledBorder("Top"));
        panelMid.setBorder(BorderFactory.createTitledBorder("Mid"));
        panelBottom.setBorder(BorderFactory.createTitledBorder("Bottom"));

        BoxLayout layout1 = new BoxLayout(panelMain, BoxLayout.Y_AXIS);

        panelMain.setLayout(layout1);


        JLabel nameLabel = new JLabel("Name: ");
        JTextField inputTextField = new JTextField(30);
        JButton submit = new JButton("Anmelden");
        JLabel displayInfo = new JLabel("Hier wird der Text vom Server displayed");

        frame.getContentPane().add(panelTop);
        frame.getContentPane().add(panelMid);
        frame.getContentPane().add(panelBottom);
        panelTop.add(nameLabel);
        panelTop.add(inputTextField);
        panelTop.add(submit);
        panelBottom.add(displayInfo);

        frame.setVisible(true);

        thread = new QunbulamanThread(panelTop);
        thread.start();


    }


}
