

import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class MulticastListener implements Runnable {

    MulticastSocket socket;

    public MulticastListener(MulticastSocket s) {
        this.socket = s;
    }

    @Override
    public void run() {

        while(true){
            try {
                byte[] buf = new byte[1000];
                DatagramPacket recv = new DatagramPacket(buf, buf.length);
                socket.receive(recv);

                byte[] recbuf = new byte[1000];
                recbuf = recv.getData();
                String response = new String(recbuf);

                if (response.startsWith("MSG")){
                    String[] tokens = response.split(" ");
                    String username = tokens[1];
                    String textMessage = tokens[2];
                    ChatGui.newMessage(username,textMessage);
                }else if (response.startsWith("JOIN")){
                    String[] tokens = response.split(" ");
                    String[] tokens2 = tokens[1].split("/");
                    String username = tokens2[0];
                    String address = tokens2[1];
                    ChatGui.memberJoined(username, address);
                }else if (response.startsWith("LEAVE")){
                    String[] tokens = response.split(" ");
                    String[] tokens2 = tokens[1].split("/");
                    String username = tokens2[0];
                    String address = tokens2[1];
                    ChatGui.memberLeaved(username, address);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
