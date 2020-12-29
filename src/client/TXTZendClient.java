package client;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class TXTZendClient {

    private JPanel panelMain;
    private JTextField textFieldIPAddress;
    private JTextField textFieldPort;
    private JTextField textFieldMessage;
    private JButton buttonSendMessage;
    private JTextField textFieldPathFile;
    private JButton buttonChooseFile;
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

    private AsynchronousSocketChannel universalSocketChannel = null;
    private File txtFile;
    private String txtPath, fileMessage;

    public TXTZendClient(){
        buttonConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textFieldIPAddress.getText().equals("") || textFieldPort.getText().equals("")){
                    JOptionPane.showMessageDialog(null,"Make sure to fill ip address and port correctly");
                }else{
                    try {
                        startClient(textFieldIPAddress.getText(), Integer.parseInt(textFieldPort.getText()));
                        textFieldStatus.setText("Connected...");
                        JOptionPane.showMessageDialog(null, "Connected to server!");
                        buttonConnect.setEnabled(false);
                    }catch (Exception exception){
                        exception.printStackTrace();
                        JOptionPane.showMessageDialog(null, exception.getMessage());
                    }
                }
            }
        });

        buttonSendMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textFieldMessage.getText().equals("")){
                    JOptionPane.showMessageDialog(null,"Please enter your message");
                }else{
                    try {
                        AtomicInteger messageWritten  = new AtomicInteger(0);
                        String message = "(" + getDate() + ") : " + textFieldMessage.getText();
                        startWrite(universalSocketChannel,message,messageWritten);
                    }catch (Exception exception){
                        JOptionPane.showMessageDialog(null,exception.getMessage());
                    }
                }
            }
        });

        buttonChooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    openFile();
            }
        });

        buttonSendFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(txtFile != null){
                    try {
                        AtomicInteger messageWritten  = new AtomicInteger(0);
                        startWrite(universalSocketChannel,fileMessage,messageWritten);
                    }catch (Exception exception){
                        JOptionPane.showMessageDialog(null,exception.getMessage());
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"Please choose file (.txt) first");
                }
            }
        });
    }


    public static void main(String[] args) {
        JFrame clientGUI = new JFrame();
        clientGUI.setContentPane(new TXTZendClient().panelMain);
        clientGUI.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        clientGUI.setTitle("TXTZendClient");
        clientGUI.setResizable(false);
        clientGUI.pack();
        clientGUI.setVisible(true);
    }

    private String getDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        return dtf.format(now);
    }

    private void startClient(String ipAddress, int port) {
        try {
            universalSocketChannel = AsynchronousSocketChannel.open();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        universalSocketChannel.connect(new InetSocketAddress(ipAddress, port), universalSocketChannel, new CompletionHandler<Void, AsynchronousSocketChannel>() {
            @Override
            public void completed(Void result, AsynchronousSocketChannel attachment) {

            }

            @Override
            public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
                System.out.println("Fail to connect to server");
            }
        });
    }

    private void startWrite(AsynchronousSocketChannel socketChannel, String message, AtomicInteger messageWritten){
        ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
        byteBuffer.put(message.getBytes());
        byteBuffer.flip();
        messageWritten.getAndIncrement();
        socketChannel.write(byteBuffer, socketChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>() {
            @Override
            public void completed(Integer result, AsynchronousSocketChannel attachment) {

            }

            @Override
            public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
                System.out.println("Fail to write message to server");
            }
        });

    }

    private void openFile(){
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter(".txt", "txt");
        fileChooser.setFileFilter(extensionFilter);
        int approve =  fileChooser.showOpenDialog(null);
        if (approve == JFileChooser.APPROVE_OPTION){
            txtPath = fileChooser.getSelectedFile().getPath();
            txtFile = new File(txtPath);
            textFieldPathFile.setText(txtPath);
            readFile();
        }
    }

    private void readFile(){
        StringBuilder finalMessage = new StringBuilder();
        try {
            Scanner reader = new Scanner(txtFile);
            while (reader.hasNextLine()) {
                finalMessage.append("(" + getDate() + ") : " + reader.nextLine() + "\n");
            }
            fileMessage = finalMessage.toString();
            System.out.println(fileMessage);
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
