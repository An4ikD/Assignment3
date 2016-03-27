import java.net.*;
import java.io.*;

public class MultiThreadedServer {
	Socket csock;
	InputStream sis = null;
	OutputStream sos = null;
	String request, response;
	byte buf[];
	int n;
	MulticastServer multicastServer = null;
	GroupData groupData;

	public MultiThreadedServer(Socket csock, MulticastServer mcastServer, GroupData gData) {
		this.csock = csock;
		buf = new byte[256];
		sis = csock.getInputStream();
		sos = csock.getOutputStream();
		multicastServer = mcastServer;
		groupData = gData;
	}

	@Override
	public void run() {
		// handle client
		
		// Send group ID, name, and number of members.
		response = "GROUPS " + groupData.id + "/" + 
												 groupData.name + "/" + 
												 groupData.members.size() + "\r\n";
		sos.write(response.getBytes());
		sos.flush();

		while (true) {
			n = sis.read();

			if (n <= 0) {
				// client has exited
				break;
			}

			request = new String(buf, 0, n);

			if (request.startsWith("WHO")) {
				// Client requests the list of group members for the group with the given ID
				response = "MEMBERS " + groupData.id + ":" + groupData.name;
				for (Map.Entry <String, String> entry : groupData.members.entrySet()) {
				    response += "/" + entry.getKey() + ":" + entry.getValue();
				}
				response += "\r\n";
				sos.write(response.getBytes());
				sos.flush();
			} else if (request.startsWith("JOIN")) {
				// Client requests to join the group with the given ID
				response = "GROUP " + groupData.id + "/" + 
									 multicastServer.address + "/" +
									 multicastServer.port + "\r\n";
				groupData.addMember()
			} else if (requests.startsWith("LEAVE")) {
				
			} else if (requests.startsWith("MSG")) {
				// At any time, the client may send a chat message.
				/*String msg = "";
				msg = request.substr(4, n);*/
				msg = request;
				multicastServer.sendMessage(msg);
			} else {
				// Invalid command!
			}
		}

	}
}