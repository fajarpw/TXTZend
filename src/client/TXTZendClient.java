package client;

import server.TXTZendServer;

import javax.swing.*;

public class TXTZendClient {

    private JPanel panelMain;
    private JTextField textFieldIPAddress;
    private JTextField textFieldPort;
    private JTextField textFieldMessage;
    private JButton buttonSendMessage;
    private JTextField textFieldPathFile;
    private JButton selectFileButton;
    private JButton buttonSendFile;
    private JLabel labelMessage;
    private JLabel labelFile;
    private JLabel labelIPAddress;
    private JLabel labelPort;
    private JTextField textFieldStatus;
    private JLabel labelStatus;
    private JButton buttonConnect;
    private JPanel panelHeader;
    private JPanel panelContent;
    private JPanel panelFooter;
    private JLabel labelFooter;

    public static void main(String[] args) {
        JFrame clientGUI = new JFrame();
        clientGUI.setContentPane(new TXTZendClient().panelMain);
        clientGUI.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        clientGUI.setTitle("TXTZendClient");
        clientGUI.setResizable(false);
        clientGUI.pack();
        clientGUI.setVisible(true);
    }
}
