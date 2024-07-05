import java.io.*;
import java.net.*;

public class Server {
    public static final int SERVICE_PORT = 12345;
    private ServerSocket server;

    public Server(ServerSocket server) {
        this.server = server;
    }

    public void startServer(){
        try {
            while (true) {
                Socket client = server.accept();
                System.out.println("Accepted From: " + client.getInetAddress() + " Port: " + client.getPort());
                ClientHandler clientHandler = new ClientHandler(client);
                clientHandler.start();
            }
        } catch (IOException ioe) {
            closeServer();
        }
    }

    public void closeServer(){
        try {
            if(server != null) server.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Cann't create the server");
        }
    }


    public static void main(String[] args) {
        SQLHelper.createDateBase();
        
        try {
            ServerSocket serverSocket = new ServerSocket(SERVICE_PORT);
            System.out.println("Server established on port: " + SERVICE_PORT);
            Server server = new Server(serverSocket);
            server.startServer();


        } catch (BindException be) {
            System.out.println("can't bind to port: " + SERVICE_PORT);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("ERROR creating server");
        }
        
    }
}