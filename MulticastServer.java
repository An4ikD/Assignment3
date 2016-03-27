
public class MulticastServer {
	
	String address = "227.3.7.3";
	MulticastSocket sock = null;
	InetAddress group = null;
	byte buf[];

	public MulticastServer() {
		try {
			group = InetAddress.getByName(address);
			sock = new MulticastSocket(4446);
			sock.joinGroup(group);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void sendMessage(String msg) {
		try {
			DatagramPacket smsg = new DatagramPacket(msg.getBytes(), 
										msg.length(), group, 4446);
			sock.send(smsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}