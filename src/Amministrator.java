import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Amministrator {
    public static void main (String[] args)  {
        String p= "true";

        if (args.length < 2) {
            System.out.println(">>Insert address and port of the server");
            System.exit(-1);
        }
        String address= args[0];
        int port= Integer.parseInt(args[1]);

        Scanner scanner= new Scanner(System.in);
        Scanner in= null;
        PrintWriter pw=null;
        Socket client = null;
        try {
            client = new Socket(address, port);
            pw=new PrintWriter(client.getOutputStream());
            pw.println(p);
            pw.flush();
            in= new Scanner(client.getInputStream());
            System.out.println(in.nextLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true){
            try {
                System.out.println(">>type in:");
                System.out.println("    >>1):quit the connection with server");
                System.out.println("    >>2):add a song");
                System.out.println("    >>3):delete a song");
                System.out.println("    >>4):read elements of list");
                System.out.println("    >>5):clear array list");
                String title;
                String nameA;
                Float d;
                String album;
                int anno;
                String toSend=null;

                int i=scanner.nextInt();
                switch(i){
                    case (1): System.out.println(">>>Closing in progress with  server at address: " + address + " and port: " + port);
                        pw.println("quit");
                        pw.flush();
                        client.close();
                        System.exit(-1);






                    case (2):
                        pw.println("add");
                        pw.flush();
                        scanner.useDelimiter("\\s*\n\\s*");
                        System.out.println("inserisci il titolo");
                        title = scanner.next();
                        System.out.println("inserisci l'autore");
                        nameA= scanner.next();
                        System.out.println("inserisci l'album");
                        album= scanner.next();
                        System.out.println("inserisci l'anno di pubblicazione");
                        anno =scanner.nextInt();
                        toSend=title+"*"+nameA+"/"+album+"**"+anno+"//";
                        pw.println(toSend);
                        pw.flush();
                        String answer=in.nextLine();
                        System.out.println(answer);
                        break;

                    case (3):// eliminare una canzone
                        pw.println("remove");
                        pw.flush();
                        scanner.useDelimiter("\\s*\n\\s*");
                        System.out.println("inserisci il titolo");
                        title = scanner.next();
                        System.out.println("inserisci l'autore");
                        nameA= scanner.next();
                        toSend=title+"*"+nameA+"/";
                        pw.println(toSend);
                        pw.flush();
                        String ans=in.nextLine();
                        System.out.println(ans+" elements in array");
                        break;
                    case (4):// get
                        pw.println("get");
                        pw.flush();
                        while (true){
                            String an = in.nextLine();
                            if (an.equalsIgnoreCase("END OF FILE"))
                                break;

                            else {
                                System.out.println("Received Data " + an);
                                //ppw.write(answer + "\n");
                                //ppw.flush();
                            }
                        }
                        break;

                    case (5):
                        pw.println("clear");
                        pw.flush();
                        String ri=in.nextLine();
                        System.out.println(ri);
                        break;


                }

            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
