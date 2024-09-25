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

                    String result = SQLHelper.checkCredentials(name, pass) ? "valid" : "not valid";
                    writer.println(result);
                }

                // for sign up operation
                if(operation.equals("sign")){
                    String name = reader.readLine();
                    String displayName = reader.readLine();
                    String pass = reader.readLine();

                    String result = SQLHelper.addUsername(name, displayName, pass);

                    if (name.isEmpty() || pass.isEmpty()) writer.println("empty");
                    else writer.println(result);
                }
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

}
