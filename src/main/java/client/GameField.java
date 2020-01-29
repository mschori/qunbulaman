package client;

import javax.swing.*;
import java.awt.*;


public class GameField extends JFrame {

    public GameField(){
        this.getContentPane().setLayout(new FlowLayout());

        JTextField input = new JTextField(20);
        input.setText("ifasdbfhs");
        add(input);


        JTextArea textArea = new JTextArea();
        textArea.setColumns(20);
        textArea.setLineWrap(true);
        textArea.setRows(5);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
    }




}
