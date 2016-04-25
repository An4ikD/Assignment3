
import java.io.*;
import java.lang.Thread;
import java.net.*;
import java.util.ArrayList;


public class Client {

    static String ADDRESS = "";
    static int PORT = 0;

    private static OutputStream sos = null;
    private static InputStream sis = null;
    private static Socket sock = null;
    public static BufferedReader br;

    public static int groupId, groupSize;
    public static String groupName;

    public static InetAddress address;
    public static MulticastSocket clientSocket;

    public static void main( String[] argv ) throws Exception {

        ChatGui chat = new ChatGui();



        // get their responses!
//        while (true) {
//            DatagramPacket recievedPacket = new DatagramPacket(buf, buf.length);
//            clientSocket.receive(recievedPacket);
//            String recievedString = new String(buf, 0, buf.length);
//            System.out.println(recievedString);
//        }

        // OK, I'm done talking - leave the group...
        //multicastSocket.leaveGroup(group);

    }

    // establish tcp connection to server and get GROUPS information
    public static void establishConnection(){
        String host = "localhost";
        int port = 7777;
        try {
            sock = new Socket( InetAddress.getByName(host), port );
            sos = sock.getOutputStream();
            sis = sock.getInputStream();

        } catch ( UnknownHostException uhe ) {
            System.out.println( "client: " + host + " cannot be resolved." );
            System.exit(-1);
        } catch ( IOException ioe ) {
            System.out.println( "client: cannot initialize socket." );
            System.exit(-1);
        }
        //String reply =  sendCommand(sock,sos,sis,"");
        try {
            br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            String response = br.readLine();
            if (response.startsWith("GROUPS")){
                String[] tokens = response.split(" ");
                String[] tokens2 = tokens[1].split("/");
                groupId = Integer.valueOf(tokens2[0]);
                groupName = tokens2[1];
                groupSize = Integer.valueOf(tokens2[2]);
            }
        } catch(IOException e){}
    }

    public static void join(int id){
        String msg = "JOIN "+id +"\r\n";
        try {
            sos.write(msg.getBytes());
        }catch (IOException e){ }
        String response = "";
        try {
            response = br.readLine();
        }catch(IOException e){}
        String[] temp = response.split(" ");
        String[] tokens = temp[1].split("/");
        //String id = tokens[0];
        ADDRESS = tokens[1];
        PORT = Integer.valueOf(tokens[2]);
    }

    // WHO id
    public static ArrayList<Member> who(int id) {
        String msg = "WHO "+ id + "\r\n";
        try {
            sos.write(msg.getBytes());
        }catch (IOException e){ }

        ArrayList<Member> members = new ArrayList<Member>();
        for(int i=0;i<groupSize+1;i++){
            String temp = "";
            try {
                temp = br.readLine();
            } catch (IOException e){}

            if (i==0) continue;
            String[] tokens = temp.split("/");
            String[] tokens2 = tokens[0].split(":");
            Member member = new Member(tokens[1],tokens2[0], tokens2[1]);
            members.add(member);
        }
        return members;
    }

    public static void connectMulticast() {
        byte[] buf = new byte[512];

        try {
            address = InetAddress.getByName(ADDRESS);
        }catch (UnknownHostException e){ }

        try {
            clientSocket = new MulticastSocket(PORT);
            clientSocket.joinGroup(address);
            new Thread(new MulticastListener(clientSocket)).start();

//            String msg = "Yerchik joined, woa";
//            DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(),address,PORT);
//            clientSocket.send(packet);
        }catch (IOException e2){ }
    }

}


