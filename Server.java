import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class Server {
	Socket csock;
	
	public static void main(String[] args) {
		int port = 7777;
		if (args.length == 1) {
			port = (new Integer(args[0])).intValue();
		} else if (args.length > 1) {
			System.out.println( "Usage: server port" );
			System.exit(-1);
		}

		GroupData groupData = null;
		MulticastServer multicastServer = null;

		groupData = new GroupData(1234, "Psy");
		multicastServer = new MulticastServer();

		ServerSocket ssock = null;

		try {
			ssock = new ServerSocket(port);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Waiting for a clients...");

		while (true) {
			try {
				Socket sock = ssock.accept();
				//csocket.getRemoteSocketAddress().toString(); get IP address of the client
				System.out.println("New client!");
				new Thread(new MultiThreadedServer(sock, multicastServer, groupData)).start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
}