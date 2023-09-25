package io.skyvoli.socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

    private static final Logger logger = LogManager.getRootLogger();
    Socket socket = new Socket("192.168.56.1", 8080);

    public Client() throws IOException {
    }

    public static void main(String[] args) throws IOException {
        new Client().send();
    }

    public void send() {
        logger.debug(socket.isBound());
        logger.debug(socket.getLocalSocketAddress());
        logger.debug(socket.getRemoteSocketAddress());
        logger.debug(socket.getLocalAddress());
        try {
            //Input
            ObjectOutputStream request = new ObjectOutputStream(socket.getOutputStream());
            request.writeObject(new TransferObject("Decode", 1, ""));
            //Output
            ObjectInputStream response = new ObjectInputStream(socket.getInputStream());
            TransferObject responseData = (TransferObject) response.readObject();
            logger.info(responseData.getDecodedText());
        } catch (IOException | ClassNotFoundException e) {
            logger.error(e.getMessage());
        }

    }
}
