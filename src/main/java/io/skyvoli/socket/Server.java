package io.skyvoli.socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final Logger logger = LogManager.getRootLogger();

    ServerSocket serverSocket = new ServerSocket(8080, 42, InetAddress.getLocalHost());

    public Server() throws IOException {
    }

    public static void main(String[] args) throws IOException {
        new Server().open();
    }

    public void open() {
        logger.debug("Address: " + serverSocket.getInetAddress());
        logger.debug("Port: " + serverSocket.getLocalPort());
        logger.debug("Bound: " + serverSocket.isBound());
        logger.debug("Closed: " + serverSocket.isClosed());
        logger.debug("Supported options: " + serverSocket.supportedOptions());
        try {
            serverSocket.setReuseAddress(true);
            while (true) {
                Socket client = serverSocket.accept();
                logger.debug("Client: " + client);
                this.handleClient(client);
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.error(e.getMessage());
        }

    }

    private void handleClient(Socket client) throws IOException, ClassNotFoundException {
        logger.info("Connection established");
        //Server does shit
        //Input
        ObjectInputStream request = new ObjectInputStream(client.getInputStream());
        TransferObject requestData = (TransferObject) request.readObject();
       //Output
        ObjectOutputStream response = new ObjectOutputStream(client.getOutputStream());
        response.writeObject(this.decodeText(requestData));
    }

    private TransferObject decodeText(TransferObject input) {
        logger.info(input.getDecodedText());
        logger.info(input.getSteps());
        logger.info(input.getText());

        return new TransferObject(input.getText(), input.getSteps(), "Nope. Don't wanna decode \"" + input.getText() + "\"");
    }
}
