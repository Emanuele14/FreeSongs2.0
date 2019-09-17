import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String f = "false";

        if (args.length < 2) {
            System.out.println(">>Insert address and port of the server");
            System.exit(-1);
        }
        String address = args[0];
        int port = Integer.parseInt(args[1]);

        Scanner scanner = new Scanner(System.in);
        Scanner in = null;
        PrintWriter pw = null;
        Socket client = null;
        try {
            client = new Socket(address, port);
            pw = new PrintWriter(client.getOutputStream());
            pw.println(f);
            pw.flush();
            in = new Scanner(client.getInputStream());
            System.out.println(in.nextLine());
            while (true) {
                System.out.println(">>type in:");
                System.out.println("    >>1):quit the connection with server");
                System.out.println("    >>2): get the songs list");
                int i = scanner.nextInt();
                switch (i) {
                    case (1):
                        System.out.println(">>>Closing in progress with  server at address: " + address + " and port: " + port);
                        pw.println("quit");
                        pw.flush();
                        client.close();
                        System.exit(-1);
                    case (2):
                        pw.println("get");
                        pw.flush();
                        while (true){
                            String answer = in.nextLine();
                            if (answer.equalsIgnoreCase("END OF FILE"))
                                break;

                            else {
                                System.out.println("Received Data " + answer);
                                //ppw.write(answer + "\n");
                                //ppw.flush();
                            }
                        }

                }

            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
