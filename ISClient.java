import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.*;

public class ISClient extends Thread implements Runnable  {

    double turnTotal = 0;
    public static void main(String[] args) throws IOException{
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter address: ");
        String address = scan.nextLine();
        System.out.println("Enter port: ");
        int port = scan.nextInt();
        System.out.println("Enter number of clients: ");
        int clientNumber = scan.nextInt();
        scan.nextLine();//flush scanner
        System.out.print("Request: ");
        String request = scan.nextLine();
        int count = 0;
        double timeTotal = 0;
        double timeSum1 = System.nanoTime();
        while(count < clientNumber) {
            count++;
            new Thread(new ISClient(address, port, count, request)).start();
        }
        double timeSum2 = System.nanoTime();
        timeTotal = timeSum2-timeSum1;
        System.out.println("Client Sum Time: " + timeTotal/1000000000);
    }

    public ISClient(String address, int port, int count, String request) {
        try {
            Socket socket = new Socket(address, port);
            System.out.println("Client " + count + " has connected.");
            Scanner input = new Scanner(System.in);
            Scanner socketScanner = new Scanner(socket.getInputStream());
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),true);
            double time1 = System.nanoTime();
            printWriter.println(request);
            BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            String serverReply="";
            while( (line = serverInput.readLine() ) != null ) {
                serverReply += line;
            }
            double time2 = System.nanoTime();
            System.out.println("Server: " + serverReply);
            double timeTaken = time2-time1;
            this.turnTotal +=timeTaken;
            System.out.println("Single Client Time: " +timeTaken/1000000000);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
