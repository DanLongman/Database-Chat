package database;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Dimension;

public class ChatFrame extends JFrame {

	private JPanel contentPane;
	private JTextField txtMessage;
	private List<JButton> channelButtons = new ArrayList<>();
	private int currentChannel;
	private int userID;
	private static List<String> usersList = new ArrayList<>();
	Timer generalTimer;
	Timer gamingTimer;
	Timer inspirationalTimer;
	private static Timer channelTimer;
	JTextArea lblWelcome;
	private JTextArea usersTextArea;
	private SignInPanel mySignInPanel;
	private JLabel lblChannelName;
	private JLabel[] messageLabels;
	private Color preferredColor;
	private JPanel usernamePanel;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatFrame frame = new ChatFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ChatFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel channelsPanel = new JPanel();
		channelsPanel.setBorder(new EmptyBorder(0, 5, 0, 5));
		contentPane.add(channelsPanel, BorderLayout.EAST);
		channelsPanel.setLayout(new GridLayout(4, 1, 0, 0));
		
		JLabel lblChannels = new JLabel("Channels:");
		lblChannels.setHorizontalAlignment(SwingConstants.CENTER);
		channelsPanel.add(lblChannels);
		
		//channelButtons.clear();  // might be needed if we call the constructor more than onc eand need to empty array list of buttons before a different set of initializations.
		try {
			for(int i = 1; i <= ChannelTable.executeQueryReturnNumOfChannels(); i++) {
				JButton channelsButton = new JButton(ChannelTable.executeQueryReturnChannelName(i));
				channelButtons.add(channelsButton);
				createBtnChannel(channelButtons.get(i-1), i);
				channelsPanel.add(channelsButton);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		JScrollPane usersScroller = new JScrollPane(usernamePanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		usersScroller.setAutoscrolls(true);
		contentPane.add(usersScroller);
		
		usernamePanel = new JPanel();
		usernamePanel.setBackground(Color.WHITE);
		usersScroller.setViewportView(usernamePanel);
		usernamePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		contentPane.add(usernamePanel, BorderLayout.WEST);
		
		usersTextArea = new JTextArea();
		usersTextArea.setText("Online Users:" + "\n");
		usernamePanel.add(usersTextArea);
		
		
		JPanel titlePanel = new JPanel();
		contentPane.add(titlePanel, BorderLayout.NORTH);
		titlePanel.setLayout(new GridLayout(2, 0, 0, 0));
		
		lblChannelName = new JLabel("Channel Name");
		lblChannelName.setHorizontalAlignment(SwingConstants.CENTER);
		titlePanel.add(lblChannelName);
		
		JPanel messageColorPanel = new JPanel();
		messageColorPanel.setLayout(new GridLayout(1, 2, 0, 0));
		contentPane.add(messageColorPanel, BorderLayout.SOUTH);
		
		txtMessage = new JTextField();
		createTxtMessageField();
		messageColorPanel.add(txtMessage);
		
		JButton btnTextColor = new JButton("text color");
		btnTextColor.addActionListener(new ActionListener() {
			int i = 0;
			public void actionPerformed(ActionEvent e) {
				Color[] myColors = {Color.BLUE, Color.PINK, Color.GREEN, Color.YELLOW, 
						Color.RED, Color.ORANGE, Color.CYAN, Color.BLACK};
				preferredColor = myColors[i];
				i = (i + 1) % 8;
			}
		});
		btnTextColor.putClientProperty("JComponent.sizeVariant", "mini");
		messageColorPanel.add(btnTextColor);
		
		JScrollPane messagesScroller = new JScrollPane(mySignInPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		messagesScroller.setAutoscrolls(true);
		contentPane.add(messagesScroller);
		
		mySignInPanel = new SignInPanel(this);
		mySignInPanel.setBackground(Color.WHITE);
		messagesScroller.setViewportView(mySignInPanel);
		
		lblWelcome = new JTextArea("Welcome !\nPlease sign-in and\nchoose a chat channel");
		
	}

	private void createTxtMessageField() {
		txtMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!txtMessage.getText().equals("") && currentChannel != 0) { // add the entered message to messages table for that channel
					try {
						ChatApp.executeSqlStatement(MessagesTable.addMessageToTable(txtMessage.getText(), "" +userID, ""+currentChannel, "yellow"));
						ChatApp.executeQueries(MessagesTable.query_All()); // prints for testing
						txtMessage.setText("");
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		txtMessage.setText("Enter Your Message Here");
		txtMessage.setColumns(10);
	}
	
	private void createBtnChannel(JButton btnChat, int channelID) {
		btnChat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentChannel = channelID;
				String channelName = ChannelTable.query_Name(channelID);
				StringBuilder generalMessages = new StringBuilder(channelName);
				generalMessages.append(" Channel:\n");
				lblChannelName.setText("General Channel");
				try (Connection connection = DriverManager.getConnection("jdbc:derby:ChatDatabase;create=true");
						Statement statement = connection.createStatement()) {
					channelTimer = new Timer(500, channelListener(generalMessages));
					channelTimer.start();
				} catch (SQLException e1) {
					e1.printStackTrace();
				} 
			}
		});
	}

	public ChatFrame getChatFrame() {
		return this;
	}
	
	public SignInPanel getSignInPanel() {
		return this.mySignInPanel;
	}
	
	public JTextArea getLblWelcome() {
		return this.lblWelcome;
	}
	
	public List<String> getUsersList() {
		return usersList;
	}
	
	public void setUserID(int ID) {
		this.userID = ID;
	}
	
	public JLabel getLblChannelName() {
		return lblChannelName;
	}
	
	private void closeChat() {
		this.dispose();
	}
	
	private void messagesUpdate(StringBuilder generalMessages) throws SQLException {
		generalMessages.delete(0, generalMessages.length());
		List<String> messages = MessagesTable.executeQueryReturnMessagesFromChannel(currentChannel);
		usersList.clear(); // clear users before updating (to avoid duplicates being created over and over)
		mySignInPanel.removeAll();
		mySignInPanel.setLayout(new GridLayout(messages.size()/2, 1, 0, 0));
		String currentUser = "";
		messageLabels = new JLabel[messages.size()/2]; // create a label for each message from channel
		for(int i = 0; i < messages.size() - 1; i+=2) { // appends the correct username based on their ID, followed by their message.
			currentUser = AccountsTable.executeQueryReturnUsername(AccountsTable.query_UserByID(Integer.parseInt(messages.get(i))));
			if(!usersList.contains(currentUser)) // create a List of user names without duplicates (to display online users)
				usersList.add(currentUser);
			messageLabels[i/2] = new JLabel(currentUser + ": " + messages.get(i + 1));
			messageLabels[i/2].setBackground(Color.WHITE);
			mySignInPanel.add(messageLabels[i/2]);
		}
		StringBuilder users = new StringBuilder("Online Users:\n");
		usersTextArea.setText(users + "");
		// adding each user-name as a JLabel
		usernamePanel.removeAll();
		usernamePanel.setLayout(new GridLayout(usersList.size() + 1, 1, 0, 0));
		usernamePanel.add(usersTextArea);
		for(int i = 0; i < usersList.size(); i ++) {
			JLabel username = new JLabel(usersList.get(i));
			username.setBackground(Color.WHITE);
			usernamePanel.add(username);
		}
	}
	
	private ActionListener channelListener(StringBuilder channelMessage) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					messagesUpdate(channelMessage);
					String currentUser = AccountsTable.executeQueryReturnUsername(AccountsTable.query_UserByID(userID));
					for(int i = 0; i < messageLabels.length; i++) {
						if(messageLabels[i].getText().substring(0, currentUser.length()).equals(currentUser))
							messageLabels[i].setForeground(preferredColor);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		};	
	}
}
