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
		// GROUPS 1234/PSY/7
		response = "GROUPS " + groupData.id + "/" + 
												 groupData.name + "/" + 
												 groupData.members.size();
		sendResponse(response);

		while (true) {
			try {
				n = sis.read(buf);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (n <= 0) {
				// client has exited
				break;
			}

			request = new String(buf, 0, n);

			System.out.println("Client sent: " + request);

			if (request.startsWith("WHO")) {
				// Client requests the list of group members for the group with the given ID
				response = "MEMBERS " + groupData.id + ":" + groupData.name;
				for (Map.Entry <String, String> entry : groupData.members.entrySet()) {
				    response += "\r\n" + entry.getKey() + "/" + entry.getValue();
				}

				System.out.println(response);
				sendResponse(response);
			} else if (request.startsWith("JOIN")) {
				// Client requests to join the group with the given ID
				response = "GROUP " + groupData.id + "/" + 
									 multicastServer.address + "/" + 
									 multicastServer.port;
				sendResponse(response);

				String username = getNextToken(request, 5, n);
				String address = csock.getRemoteSocketAddress().toString();
				address = address.substring(1);
				groupData.addMember(username, address);

				String msg = "JOIN " + username + "/" + address;
				multicastServer.sendMessage(msg);
			} else if (request.startsWith("LEAVE")) {
				String address = csock.getRemoteSocketAddress().toString();
				String username = groupData.getUsername(address);
				groupData.removeMember(username, address);

				String msg = "LEAVE " + username + "/" + address;
				multicastServer.sendMessage(msg);
				sendResponse("");
			} else if (request.startsWith("MSG")) {
				// At any time, the client may send a chat message.
				String address = csock.getRemoteSocketAddress().toString();
				String username = groupData.getUsername(address);

				String msg = "MSG " + username + "@" + address + " ";
				msg += request.substring(4, n);
				multicastServer.sendMessage(msg);
				sendResponse("");
			} else {
				sendResponse("Invalid command!");
			}
		}
	}

	public void sendResponse(String response) {
		response += "\r\n";
		try {	
			sos.write(response.getBytes());
			sos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getNextToken(String s, int pos, int n) {
		String res = "";
		for (int i = pos; i < n; i++) {
			if (s.charAt(i) == ':' || s.charAt(i) == '/') {
				break;
			} else {
				res += s.charAt(i);
			}
		}
		return res;
	}
}
