package server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

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
    private JTextField textFieldMessageReceived;
    private JTextArea textArea1;
    private JLabel labelServerStatus;

    public TXTZendServer(){
        buttonListen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    new Thread(() -> {
                        try {
                            EchoServer(textFieldIPAdrress.getText(), Integer.parseInt(textFieldPort.getText()));
                            textFieldServerStatus.setText("Server listening...");
                            JOptionPane.showMessageDialog(null,"Server start to listening...");
                            buttonListen.setEnabled(false);
                    }catch (IOException ioException){
                            ioException.printStackTrace();
                    }
                    }).start();
                }catch (Exception exception){
                    JOptionPane.showMessageDialog(null, exception.getMessage());
                }
            }
        });
    }

    private void EchoServer(String ipAddress, int port) throws IOException{
        InetSocketAddress socketAddress = new InetSocketAddress(ipAddress, port);
        AsynchronousServerSocketChannel serverSocket = AsynchronousServerSocketChannel.open().bind(socketAddress);
        serverSocket.accept(serverSocket, new CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel>() {
            @Override
            public void completed(AsynchronousSocketChannel result, AsynchronousServerSocketChannel attachment) {
                serverSocket.accept(serverSocket,this);
                startRead(result);
            }

            @Override
            public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
                System.out.println("Fail to accept connection");
            }
        });
    }

    private void startRead(AsynchronousSocketChannel socketChannel){
       ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
       socketChannel.read(byteBuffer, socketChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>() {
           @Override
           public void completed(Integer result, AsynchronousSocketChannel attachment) {
               byteBuffer.flip();
               startRead(attachment);
               textFieldMessageReceived.setText(new String(byteBuffer.array()));
           }

           @Override
           public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
               System.out.println("Fail to read messsage from client");
           }
       });
    }

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
