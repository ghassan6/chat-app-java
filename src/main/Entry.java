package main;
import java.io.*;
import java.net.*;

public class Entry extends Thread {
    Socket client;
    public Entry(Socket client){
        this.client = client;
    }

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

                    if(SQLHelper.checkCredentials(name, pass)) {
                        System.out.println("valid");
                        writer.println("valid");
                    }

                    else {
                        System.out.println("not");
                        writer.println("not valid");
                    }
                }

                // for sign up operation
                if(operation.equals("sign")){
                    String name = reader.readLine();
                    String displayName = reader.readLine();
                    String pass = reader.readLine();
                    System.out.println(name + " " + displayName + " " + pass);

                    if(SQLHelper.addUsername(name, displayName, pass).equals("taken")){
                        System.out.println("taken");
                        writer.println("taken");
                    }

                    else if (SQLHelper.addUsername(name, displayName, pass).equals("valid")) {
                        // TO DO: send something else to the signup controller
                        writer.println("Valid");
                    }

                }
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

}
