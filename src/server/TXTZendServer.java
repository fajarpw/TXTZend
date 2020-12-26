package server;

import javax.swing.*;

public class TXTZendServer {
    private JPanel panelMain;
    private JPanel panelHeader;
    private JPanel panelContent;
    private JPanel panelFooter;
    private JLabel labelFooter;
    private JTextField textFieldIPAdrress;
    private JTextField textFieldPort;
    private JButton buttonListen;
    private JTextField textFieldServerStatus;
    private JLabel labelPort;
    private JTextField textField1;
    private JTextArea textArea1;
    private JLabel labelServerStatus;


    public static void main(String[] args) {
        JFrame clientGUI = new JFrame();
        clientGUI.setContentPane(new TXTZendServer().panelMain);
        clientGUI.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        clientGUI.setTitle("TXTZendServer");
        clientGUI.setResizable(false);
        clientGUI.pack();
        clientGUI.setVisible(true);
    }

}
