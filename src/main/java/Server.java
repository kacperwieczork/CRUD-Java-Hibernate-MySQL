import dto.UserOperation;
import dto.UserOperationReponse;

import java.io.*;
import java.net.*;

public class Server {
    public static ServerSocket serverSocket;

    public Server() {
    }

    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                OperationProcessor operationProcessor = new OperationProcessor();

                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                InputStream is = socket.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                UserOperation request = (UserOperation) ois.readObject();
                UserOperationReponse response = operationProcessor.process(request);
                out.println(response.getResponse());
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
