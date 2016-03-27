import java.net.*;
import java.io.*;
import java.util.Map;

public class MultiThreadedServer implements Runnable {
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
		try {	
			sis = csock.getInputStream();
			sos = csock.getOutputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
		multicastServer = mcastServer;
		groupData = gData;
	}

	@Override
	public void run() {
		// handle client
		
		// Send group ID, name, and number of members.
		response = "GROUPS " + groupData.id + "/" + 
												 groupData.name + "/" + 
												 groupData.members.size();
		sendResponse(response);

		while (true) {
			try {
				n = sis.read();
			} catch (Exception e) {
				e.printStackTrace();
			}
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
				sendResponse(response);
			} else if (request.startsWith("JOIN")) {
				// Client requests to join the group with the given ID
				response = "GROUP " + groupData.id + "/" + 
									 multicastServer.address + "/" +
									 multicastServer.sock;
				sendResponse(response);
				//groupData.addMember();
			} else if (request.startsWith("LEAVE")) {
				
			} else if (request.startsWith("MSG")) {
				// At any time, the client may send a chat message.
				/*String msg = "";
				msg = request.substr(4, n);*/
				String msg = request;
				multicastServer.sendMessage(msg);
			} else {
				// Invalid command!
			}
		}
	}

	void sendResponse(String response) {
		response += "\r\n";
		try {	
			sos.write(response.getBytes());
			sos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
