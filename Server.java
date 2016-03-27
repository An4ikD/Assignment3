import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Server {
	Socket csock;
	int port = 7777;
	MulticastServer multicastServer = null;
	GroupData groupData = null;

	public static void main(String[] args) {

		if (args.length == 1) {
			port = (new Integer(argv[0])).intValue();
		} else if (args.length > 1) {
			System.out.println( "Usage: server port" );
			System.exit(-1);
		}

		groupData = new GroupData(1234, "Psy");
		multicastServer = new MulticastServer();

		ServerSocket ssock = new ServerSocket(port);
		System.out.println("Waiting for a clients...");

		while (true) {
			Socket sock = ssock.accept();
			//csocket.getRemoteSocketAddress().toString(); get IP address of the client
			System.out.println("New client!");
			new Thread(new MultiThreadedServer(sock, multicastServer, groupData)).start();
		}
	}

	
}