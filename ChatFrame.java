package database;

/******************************************
 * CSIS2410: Advanced Programming
 * Assignment 02: Database Application
 * Authors: Joshua DeMoss & Daniel Longman
 * Date: 10/07/2018
 ******************************************/

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JScrollBar;

/**
 * This JFrame contains all of the chat application appearance and interaction.
 * @author Joshua DeMoss & Daniel Longman
 *
 */
public class ChatFrame extends JFrame {

	private JPanel contentPane;
	private JTextField txtMessage;
	private List<JButton> channelButtons = new ArrayList<>();
	private int currentChannel;
	private int userID;
	Timer generalTimer;
	Timer gamingTimer;
	Timer inspirationalTimer;
	private static Timer channelTimer;
	JTextArea lblWelcome;
	private SignInPanel mySignInPanel;
	private JLabel lblChannelName;
	private String preferredColorName = "black";
	private JPanel usernamePanel;
	private boolean autoScroll = true;
	private JScrollPane messagesScroller;

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
		channelsPanel.setLayout(new BoxLayout(channelsPanel, BoxLayout.Y_AXIS));

		JLabel lblChannels = new JLabel("Channels:");
		lblChannels.setFont(new Font("Courier", Font.BOLD, 20));
		lblChannels.setHorizontalAlignment(SwingConstants.CENTER);
		channelsPanel.add(lblChannels);

		for (int i = 1; i <= ChannelTable.numOfChannels(); i++) {
			JButton channelsButton = new JButton(ChannelTable.getChannelName(i));
			channelButtons.add(channelsButton);
			createBtnChannel(channelsButton, i);
			channelsPanel.add(channelsButton);
			channelsButton.setMinimumSize(new Dimension(150, 50));
			channelsButton.setMaximumSize(new Dimension(150, 200));
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

		JPanel titlePanel = new JPanel();
		contentPane.add(titlePanel, BorderLayout.NORTH);
		titlePanel.setLayout(new GridLayout(2, 0, 0, 0));

		lblChannelName = new JLabel("Channel Name");
		lblChannelName.setFont(new Font("Courier", Font.BOLD, 20));
		lblChannelName.setHorizontalAlignment(SwingConstants.CENTER);
		titlePanel.add(lblChannelName);

		JPanel messageColorPanel = new JPanel();
		messageColorPanel.setLayout(new BoxLayout(messageColorPanel, BoxLayout.X_AXIS));
		contentPane.add(messageColorPanel, BorderLayout.SOUTH);

		txtMessage = new JTextField();
		createTxtMessageField();
		messageColorPanel.add(txtMessage);

		JButton btnTextColor = new JButton("Text Color");
		createBtnTextColor(btnTextColor);
		messageColorPanel.add(btnTextColor);

		messagesScroller = new JScrollPane(mySignInPanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		messagesScroller.setAutoscrolls(true);
		contentPane.add(messagesScroller);

		mySignInPanel = new SignInPanel(this);
		mySignInPanel.setBackground(Color.WHITE);
		mySignInPanel.setLayout(new BoxLayout(mySignInPanel, BoxLayout.Y_AXIS));
		messagesScroller.setViewportView(mySignInPanel);

		lblWelcome = new JTextArea("Welcome !\nPlease sign-in and\nchoose a chat channel");

		channelTimer = new Timer(200, channelListener());
		channelTimer.start();

	}

	private void createBtnTextColor(JButton btnTextColor) {
		btnTextColor.setPreferredSize(new Dimension(130, 20));
		btnTextColor.addActionListener(new ActionListener() {
			int i = 0;
			@Override
			public void actionPerformed(ActionEvent e) {
				Color[] myColors = {Color.BLUE, Color.PINK, Color.GREEN, Color.MAGENTA,
					Color.RED, Color.ORANGE, Color.CYAN, Color.BLACK};
				String[] colorNames = {"blue", "pink", "green", "magenta", "red", "orange", "cyan", "black"};
				preferredColorName = colorNames[i];
				btnTextColor.setForeground(myColors[i]);
				i = (i + 1) % 8;
			}
		});
		btnTextColor.putClientProperty("JComponent.sizeVariant", "mini");
	}

	private void createTxtMessageField() {
		txtMessage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!txtMessage.getText().equals("") && currentChannel != 0) { // add the entered message to messages table for that channel
					try {
						String filteredString = txtMessage.getText().replace("\\", "\\\\").replace("'", "\\'");
						SQLManager.executeSqlStatement(MessagesTable.addMessageToTable(filteredString, userID, currentChannel, preferredColorName));
						AccountsTable.updateOnlineTime(userID);
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
			@Override
			public void actionPerformed(ActionEvent e) {
				currentChannel = channelID;
				lblChannelName.setText(ChannelTable.getChannelName(channelID) + " Channel");
			}
		});
	}

	/**
	 * Returns this instance of the chat frame.
	 * @return
	 */
	public ChatFrame getChatFrame() {
		return this;
	}

	/**
	 * Returns the sign in panel.
	 * @return
	 */
	public SignInPanel getSignInPanel() {
		return this.mySignInPanel;
	}

	/**
	 * Returns the welcome message as a JTextArea.
	 * @return
	 */
	public JTextArea getLblWelcome() {
		return this.lblWelcome;
	}

	/**
	 * Sets the user ID to the specified value.
	 * @param ID
	 */
	public void setUserID(int ID) {
		this.userID = ID;
	}

	/**
	 * Returns the channelName JLabel.
	 * @return
	 */
	public JLabel getLblChannelName() {
		return lblChannelName;
	}

	private void messagesUpdate() {
		//See if we should autoscroll after this update
		messagesScroller.validate();
		JScrollBar scroll = messagesScroller.getVerticalScrollBar();
		autoScroll = scroll.getValue() > scroll.getMaximum() - scroll.getVisibleAmount() - 5;

		List<Message> messages = MessagesTable.getMessagesFromChannel(currentChannel);
		mySignInPanel.removeAll();
		String currentUser;
		for (int i = 0; i < messages.size(); i++) { // appends the correct username based on their ID, followed by their message.
			currentUser = messages.get(i).user;
			JLabel newMessage = new JLabel(messages.get(i).time + " " + currentUser + ": " + messages.get(i).messageText);
			newMessage.setBackground(Color.WHITE);
			newMessage.setForeground(messages.get(i).color);
			mySignInPanel.add(newMessage);
		}
		// adding each user-name as a JLabel
		List<String> onlineUsers = AccountsTable.getOnlineUsers();
		usernamePanel.removeAll();
		usernamePanel.setLayout(new GridLayout(Math.max(10, onlineUsers.size() + 1), 1, 0, 0));
		usernamePanel.add(new JLabel("Online Users:"));
		for (int i = 0; i < onlineUsers.size(); i++) {
			JLabel username = new JLabel(onlineUsers.get(i));
			username.setBackground(Color.WHITE);
			usernamePanel.add(username);
		}
		if (autoScroll) {
			messagesScroller.validate();
			messagesScroller.getVerticalScrollBar().setValue(messagesScroller.getVerticalScrollBar().getMaximum());
		}
		contentPane.validate();
	}

	private ActionListener channelListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (currentChannel != 0) {
					messagesUpdate();
				}
			}
		};
	}
}
