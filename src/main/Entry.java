package main;
import java.io.*;
import java.net.*;

public class Entry extends Thread {
    Socket client;
    public Entry(Socket client) { this.client = client; }

    @Override
    public void run(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(client.getOutputStream()) , true);

            while (true) {
                String operation = reader.readLine();

                // for log-in operation
                if(operation.equals("log")){
                    String name = reader.readLine();
                    String pass = reader.readLine();

                    String response = SQLHelper.checkCredentials(name, pass) ? "valid" : "not valid";
                    writer.println(response);
                }

                // for sign up operation
                if(operation.equals("sign")){
                    String name = reader.readLine();
                    String displayName = reader.readLine();
                    String pass = reader.readLine();

                    String response = SQLHelper.addUsername(name, displayName, pass);
                    writer.println(response);
                }
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
}
