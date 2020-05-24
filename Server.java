import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;
import static java.lang.System.in;

public class Server {
/*** NESTED CLASS : ConnectionHandler ***/
    private class ConnectionHandler implements Runnable{
        Socket clientSocket;
        ConnectionHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

    @Override
    public void run() {
            try {
                System.out.println("THREAD CREATED & CLIENT : " + this.clientSocket.getInetAddress() + " CONNECTED ");
                BufferedReader clientInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter serverOutput = new PrintWriter(clientSocket.getOutputStream());
                ProcessAndExecute(clientInput, serverOutput);
                System.out.print("Closing thread...");
                serverOutput.close();
                clientInput.close();
                clientSocket.close();
                System.out.println("thread closed");
            }
            catch (Exception e){
                e.printStackTrace();
            }
    }
    private void ProcessAndExecute(BufferedReader clientInput, PrintWriter serverOutput){
            try {
                Runtime runtime = Runtime.getRuntime();
                Process process = null;
                String request = clientInput.readLine();
                System.out.println("Executing " + request + "... ");
                switch (request) {
                    case "date and time" :
                        process = runtime.exec("date");
                        break;
                    case "uptime" :
                        process = runtime.exec("uptime");
                        break;
                    case "memory use" :
                        process = runtime.exec("free");
                        break;
                    case "netstat" :
                        process = runtime.exec("netstat -an");
                        break;
                    case "current users" :
                        process = runtime.exec("users");
                        break;
                    case "running processes" :
                        process = runtime.exec("ps -e");
                        break;
                    case "exit" :
                        System.out.println("exiting execution environment...");
                        return;
                }
                    BufferedReader processReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String result="";
                    while( (result = processReader.readLine()) != null ){
                        serverOutput.println(result);
                    }
                    System.out.println();
            }
            catch(Exception e){
                e.printStackTrace();
            }
    }
}
/*** END NESTED CLASS ***/

    // DATA FIELDS
    private ServerSocket serverSocket;
    private Socket clientSocket;
    // CONSTRUCTOR
    public Server(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            this.clientSocket = new Socket("localhost", port);
            boolean up = true;
            while(up){
                System.out.print("now searching for connections on port " + port + "... ");
                this.clientSocket = serverSocket.accept();
                new Thread(new ConnectionHandler(clientSocket)).start();
                System.out.println("socket accepted, creating new connection thread...");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /*** BEGIN MAIN METHOD ***/
    public static void main(String[] args) {
        int port=-1;
        boolean portValid=false;
        Scanner s = new Scanner(in);
        //take valid user input
        while(portValid==false){
            System.out.print("Enter a port number to listen on: ");
            port = s.nextInt();
            if (port >= 0 && port <= 65535){
                    portValid=true;
            }
            else{
                System.out.println("Please enter a valid port number... Try again,");
                s.next();
            }
        }

        System.out.println("Starting up the Server...");
        Server server = new Server(port);
    }
    /*** END MAIN METHOD ***/
}
