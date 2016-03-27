
public class GroupData {
	int id;
	String name;
	Map <String, String> members = new HashMap <String, String>();

	public GroupData(int id, String name) {
		this.id = id;
		this.name = name;
	}
}