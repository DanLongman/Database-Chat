package database;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class SignInPanel extends JPanel {
	private JTextField textFieldUsername;
	private JTextField textFieldPassword;
	private int password;
	private int userID;
	
	/**
	 * Create the panel.
	 */
	public SignInPanel(ChatFrame myChatFrame) {
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(0, 0));
		
		JPanel signInPanel = new JPanel();
		add(signInPanel, BorderLayout.CENTER);
		signInPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblEnterUsername = new JLabel("Please enter your username:");
		lblEnterUsername.setHorizontalAlignment(SwingConstants.CENTER);
		signInPanel.add(lblEnterUsername);
		
		textFieldUsername = new JTextField();
		textFieldUsername.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				textFieldUsername.setText(textFieldUsername.getText());
			}
		});
		signInPanel.add(textFieldUsername);
		textFieldUsername.setColumns(10);
		
		JLabel lblPassword = new JLabel("Please enter a 4-digit password:");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		signInPanel.add(lblPassword);
		
		textFieldPassword = new JTextField();
		textFieldPassword.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				textFieldUsername.setText(textFieldUsername.getText());
			}
		});
		signInPanel.add(textFieldPassword);
		textFieldPassword.setColumns(10);
		
		JButton btnSignIn = new JButton("Sign In/Up");
		btnSignIn.setOpaque(true);
		btnSignIn.setBackground(Color.BLUE);
		btnSignIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					 userID = 0;
					try(Connection connection = DriverManager.getConnection("jdbc:derby:ChatDatabase;create=true");
							Statement statement = connection.createStatement()) {
						String userName = textFieldUsername.getText();
						password = Integer.parseInt(textFieldPassword.getText());
						// get the password of this username from database if there is such a username
						int actualPassword = AccountsTable.executeQueryReturnPassword(userName);
						int actualID = AccountsTable.executeQueryReturnUserID(userName);
						if(actualID == 0) { // creates the account if no user with this user name exists.
							ChatApp.executeSqlStatement(AccountsTable.addAcount(userName, ""+password));
							userID = (AccountsTable.executeQueryReturnUserID(userName));
							myChatFrame.setUserID(userID);
							myChatFrame.getSignInPanel().removeAll();
							myChatFrame.getSignInPanel().add(myChatFrame.getLblWelcome());
							myChatFrame.getLblWelcome().setText("Welcome " + userName +"!\n Please choose a channel to chat in...");
						}
						// check if passwords match
						else if(actualPassword != password) { // This means there is a different password associated with this user name.
							JOptionPane.showMessageDialog(null, "You either entered a wrong password\n"
								+ "or this username is taken."); 
						}
						// this is reached if passwords match
						else {
							userID = (AccountsTable.executeQueryReturnUserID(userName));
							myChatFrame.setUserID(userID);
							myChatFrame.getSignInPanel().removeAll();
							myChatFrame.getSignInPanel().add(myChatFrame.getLblWelcome()); 
							myChatFrame.getLblWelcome().setText("Welcome back " + userName +"!\n Please choose a channel to chat in...");
						}
						
					} catch (SQLException e3) {
						e3.printStackTrace();
					}
					System.out.println(textFieldUsername.getText() + " password:" + textFieldPassword.getText());
					try(Connection connection = DriverManager.getConnection("jdbc:derby:ChatDatabase;create=true");
							Statement statement = connection.createStatement()) {
						System.out.println(userID);
						
					} catch (SQLException e2) {
						e2.printStackTrace();
					}
			}
		});
		btnSignIn.setBorder(new EmptyBorder(0, 10, 0, 10));
		add(btnSignIn, BorderLayout.SOUTH);
	}

	public int getUserID() {
		return userID;
	}
}
