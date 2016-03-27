import java.util.HashMap;
import java.util.Map;

public class GroupData {
	int id;
	String name;
	Map <String, String> members = new HashMap <String, String>();

	public GroupData(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public void addMember(String username, String address) {
		members.put(address, username);
	}

	public String getUsername(String address) {
		return members.get(address);
	}

	public void removeMember(String username, String address) {
		members.remove(address);
	}

}