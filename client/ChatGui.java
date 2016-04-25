
import javax.swing.*;
import javax.swing.JTextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ChatGui extends JFrame{
    public static JFrame gui = new JFrame();
    public static JList<Member> members;

    public static JTextArea chatTextArea;

    public ChatGui() {

        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setSize(1200, 700);
        gui.setLayout(null);
        gui.setLocationRelativeTo(null);
        gui.setTitle("Multicast Chat");


        chatTextArea = new JTextArea();
        chatTextArea.setLocation(50,50);
        chatTextArea.setSize(500,400);
        chatTextArea.setEditable(false);
        gui.add(chatTextArea);

        members = new JList();
        members.setLocation(555, 50);
        members.setSize(300, 400);
        members.setVisible(true);
        //emailsList.setModel(listModel);
        add(new JScrollPane(members));
        gui.add(members);

        final JTextField memberNameField = new JTextField("Enter your name");
        memberNameField.setLocation(890,50);
        memberNameField.setSize(250,40);
        gui.add(memberNameField);

        final JButton joinChat = new JButton("Join/Leave");
        joinChat.setLocation(890,90);
        joinChat.setSize(250, 40);
        gui.add(joinChat);

        final JTextField messageField = new JTextField("Enter your message");
        messageField.setLocation(50,500);
        messageField.setSize(500,40);
        gui.add(messageField);

        final JButton sendMessage = new JButton("Send message");
        sendMessage.setLocation(600,500);
        sendMessage.setSize(150,40);
        gui.add(sendMessage);

        final JButton exit = new JButton("Exit");
        exit.setLocation(50,600);
        exit.setSize(150,40);
        gui.add(exit);

        joinChat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = memberNameField.getText();
                Client.establishConnection();
                Client.join(Client.groupId);
                Client.connectMulticast();

                final DefaultListModel<Member> membersListModel = new DefaultListModel<Member>();
                ArrayList<Member> membersList = Client.who(Client.groupId);
                for (int i=0;i<membersList.size();i++){
                    membersListModel.addElement(membersList.get(i));
                }
                members.setModel(membersListModel);
            }
        });
        gui.setVisible(true);
    }

    public static void newMessage(String username, String text){
        String msg = username + ": " + text +"\n";
        chatTextArea.append(msg);
    }

    public static void memberJoined(String username, String address){
        String msg = username +" Joined the chat";
        chatTextArea.append(msg);
    }

    public static void memberLeaved(String username, String address){
        String msg = username + "Leaved the chat";
        chatTextArea.append(msg);
    }

}
