import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Server {
    private ArrayList<Song> songs = new ArrayList<>();

    public synchronized ArrayList<Song> getSongs() {
        return songs;
    }

    public synchronized void setSongs(Song T) {
        this.songs.add(T);
    }

    int port;

    public Server(int port) {
        this.port = port;
    }
    public void go() {
        ServerSocket server= null;
        try {
            System.out.println("Starting server on port:" + port);
            server = new ServerSocket(port);
            System.out.println("Server started");

        } catch(IOException e)

        {
            e.printStackTrace();
            System.out.println("Server cannot start on port"+port);
            System.exit(-1);
        }
        Saver savingFile = new Saver();
        Thread S= new Thread(savingFile);
        S.start();
        while(true){

            try {
                System.out.println("\n>>>SERVER:Waiting for connection...");
                Socket client = server.accept();
                Scanner in= new Scanner(client.getInputStream());

                String val=in.next();

                if(val.equalsIgnoreCase("true")) {

                    //System.out.println("Connection: OK");
                    ClientManager cm = new ClientManager(client);//client manager per il client amministratore
                    Thread t = new Thread(cm);
                    t.start();
                }
                else if(val.equalsIgnoreCase("false")){
                    ClientManagerR cmr = new ClientManagerR(client);// clientManager per il client senza permessi
                    Thread x = new Thread(cmr);
                    x.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
    public class ClientManager implements Runnable{
        Socket myClient;

        public ClientManager(Socket myClient) {
            this.myClient = myClient;
        }

        @Override
        public void run() {
            System.out.println(">>>ClientManager:"+Thread.currentThread().getName()+"started");
            System.out.println(">>>ClientManager: connection with amministrator");
            Scanner in= null;

            try {
                in = new Scanner(myClient.getInputStream());
                PrintWriter pw= new PrintWriter(myClient.getOutputStream());
                pw.println(">>welcome to the reserved area");
                pw.flush();

                while(true) {
                    String command = in.nextLine();
                    if (command.equalsIgnoreCase("quit")) {
                        System.out.println(">>>ClientManager:" + Thread.currentThread().getName() + "closing connection with amministrator");
                        myClient.close();
                        break;
                    } else if (command.equalsIgnoreCase("add")) {
                        System.out.println("    >>>ClientManager:operation in progress ->add");
                        String message = in.nextLine();
                        //System.out.println(message);
                        int endtitolo = message.indexOf("*");
                        int endautore = message.indexOf("/");
                        int endalbum = message.indexOf("**");
                        int endanno = message.indexOf("//");
                        String titolo = message.substring(0, endtitolo);
                        String autore = message.substring(endtitolo + 1, endautore);
                        String album = message.substring(endautore + 1, endalbum);
                        int anno = Integer.parseInt(message.substring(endalbum + 2, endanno));
                        Song s = new Song();
                        s.setTitle(titolo);
                        s.setNameAuthor(autore);
                        s.setNameAlbum(album);
                        s.setYear(anno);
                        songs.add(s);
                        pw.println("    >>>ClientManager:" + Thread.currentThread().getName() + ":operation completed successfully");
                        pw.flush();
                        //System.out.println(s.getTitle());
                        //System.out.println(s.getNameAuthor());
                        //System.out.println(s.getNameAlbum());
                        //System.out.println(s.getYear());
                        System.out.println("    >>>ClientManager:" + "added song");
                    } else if (command.equalsIgnoreCase("clear")) {
                        System.out.println("    >>>ClientManager:operation in progress ->clear");
                        Thread.sleep(1000);
                        System.out.println("    >>>ClientManager:arrayList has" + songs.size() + "elements");
                        songs.clear();
                        Thread.sleep(1000);
                        pw.println("    >>>ClientManager:" + Thread.currentThread().getName() + ":operation completed successfully. There are "+songs.size()+ "elements");
                        pw.flush();
                        System.out.println("    >>>ClientManager:arrayList emptied  " + songs.size() + "  elements");

                    } else if (command.equalsIgnoreCase("remove")) {
                        System.out.println("    >>>ClientManager:operation in progress ->remove");
                        String message = in.nextLine();
                        //System.out.println(message);
                        int endtitolo = message.indexOf("*");
                        int endautore = message.indexOf("/");
                        String titolo = message.substring(0, endtitolo);
                        String autore = message.substring(endtitolo + 1, endautore);
                        Song s = new Song();
                        s.setTitle(titolo);
                        s.setNameAuthor(autore);
                        for (int i = 0; i < songs.size(); i++) {

                            int r = songs.get(i).compareTo(s); // se è uguale a 0 ho individuato la canzone con l'autore cercato
                            //System.out.println(r);
                            //System.out.println(songs.get(i).getTitle());
                            //System.out.println(songs.get(i).getNameAuthor());
                            //System.out.println(titolo);
                            if (r == 0) {// poichè il compareTo delle stringhe torna zero se sono nella stessa posizione

                                //System.out.println(songs.get(i).getTitle());
                                //System.out.println(songs.get(i).getNameAuthor());
                                songs.remove(songs.get(i));//adesso che so l'indice elimino la canzone
                                System.out.println("    >>>ClientManager: " + titolo + " of " + autore+" is removed ");
                            }
                            else System.out.println("    >>>ClientManager: song not found" );

                            pw.println("    >>>ClientManager:" + Thread.currentThread().getName() + ":operation completed successfully. There are "+songs.size());
                            pw.flush();

                        }

                    }else if(command.equalsIgnoreCase("get")){
                        System.out.println("    >>>ClientManager:operation in progress ->get");
                        Scanner freader= new Scanner(new FileReader("SongsList.txt"));
                        String toSend;
                        while(freader.hasNextLine()) {
                            toSend = freader.nextLine();
                            if(toSend==null) break;
                            else{
                                pw.println(toSend);
                                pw.flush();
                                System.out.println("    >>>SENT: " + toSend);
                            }
                            pw.println("END OF FILE");
                            pw.flush();



                        }
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }
    class  Saver implements Runnable{

        public void run(){
            System.out.println(">>>Thread SAVER: saving file periodically");

            try {
                while(true){
                    Thread.sleep(10000);
                    realSaver sv =new realSaver();
                    Thread th =new Thread(sv);
                    th.run();

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }





        }
    }
    class realSaver implements Runnable{
        public void run(){
            File f =new File("SongsList.txt");
            FileWriter fw= null;
            try {
                fw = new FileWriter(f, false);
                fw.write(getSongs().toString());
                fw.flush();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("File don't create");
            }


        }
    }
    public class ClientManagerR implements Runnable{
        Socket myClientR;

        public ClientManagerR(Socket myClientR) {
            this.myClientR = myClientR;
        }
        @Override
        public void run() {
            System.out.println(">>>ClientManagerR:"+Thread.currentThread().getName()+"started");
            System.out.println(">>>ClientManagerR: connection with client");
            Scanner in= null;
            try {
                in = new Scanner(myClientR.getInputStream());
                PrintWriter pw= new PrintWriter(myClientR.getOutputStream());
                pw.println(">>welcome client area");
                pw.flush();
                while(true) {
                    String command = in.nextLine();
                    if (command.equalsIgnoreCase("quit")) {
                        System.out.println(">>>ClientManagerR:" + Thread.currentThread().getName() + "closing connection with client");
                        myClientR.close();
                        break;
                    }
                    else if(command.equalsIgnoreCase("get")){
                        System.out.println("    >>>ClientManagerR:operation in progress ->get");
                        Scanner freader= new Scanner(new FileReader("SongsList.txt"));
                        String toSend;
                        while(freader.hasNextLine()) {
                            toSend = freader.nextLine();
                            if(toSend==null) break;
                            else{
                                pw.println(toSend);
                                pw.flush();
                                System.out.println("    >>>SENT: " + toSend);
                            }
                            pw.println("END OF FILE");
                            pw.flush();



                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                //} catch (InterruptedException e) {
                //e.printStackTrace();
            }

        }
    }

}
