package database;

/******************************************
 * CSIS2410: Advanced Programming
 * Assignment 02: Database Application
 * Authors: Joshua DeMoss & Daniel Longman
 * Date: 10/07/2018
 ******************************************/

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.BoxLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * Provides the JPanel to allow the user to sign in/up to the chat.
 * @authors Joshua DeMoss & Daniel Longman
 *
 */
@SuppressWarnings("serial")
public class SignInPanel extends JPanel {

	private JTextField textFieldUsername;
	private JPasswordField textFieldPassword;
	private int password;
	private int userID;

	/**
	 * Creates the panel.
	 */
	public SignInPanel(ChatFrame myChatFrame) {
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new GridLayout(3, 1));

		JPanel usernameInfo = new JPanel(new GridLayout(1, 2));

		JPanel usernameHolder = new JPanel();
		usernameHolder.setLayout(new BoxLayout(usernameHolder, BoxLayout.X_AXIS));

		JLabel lblEnterUsername = new JLabel("Please enter your username:");
		lblEnterUsername.setHorizontalAlignment(SwingConstants.RIGHT);

		textFieldUsername = new JTextField();
		textFieldUsername.setMaximumSize(new Dimension(300, 50));

		usernameInfo.add(lblEnterUsername);
		usernameHolder.add(textFieldUsername);

		usernameInfo.add(usernameHolder);

		add(usernameInfo);

		JPanel passwordInfo = new JPanel(new GridLayout(1, 2));

		JPanel passwordHolder = new JPanel();
		passwordHolder.setLayout(new BoxLayout(passwordHolder, BoxLayout.X_AXIS));

		JLabel lblPassword = new JLabel("Please enter a 4-digit password:");
		lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);

		textFieldPassword = new JPasswordField();
		textFieldPassword.setMaximumSize(new Dimension(300, 50));

		passwordInfo.add(lblPassword);
		passwordHolder.add(textFieldPassword);

		passwordInfo.add(passwordHolder);

		add(passwordInfo);
		
		JPanel signInHolder = new JPanel();
		signInHolder.setLayout(new BoxLayout(signInHolder, BoxLayout.Y_AXIS));
		signInHolder.setAlignmentX(CENTER_ALIGNMENT);

		JButton btnSignIn = new JButton("Sign In/Up");
		creatBtnSignIn(myChatFrame, btnSignIn);
		signInHolder.add(btnSignIn);
		add(signInHolder);
	}

	private void creatBtnSignIn(ChatFrame myChatFrame, JButton btnSignIn) {
		btnSignIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				userID = 0;
				String userName = textFieldUsername.getText();
				password = Integer.parseInt(textFieldPassword.getText());
				// get the password of this username from database if there is such a username
				int actualPassword = AccountsTable.executeQueryReturnPassword(userName);
				int actualID = AccountsTable.executeQueryReturnUserID(userName);
				if (actualID == 0) {
					try {
						// creates the account if no user with this user name exists.
						SQLManager.executeSqlStatement(AccountsTable.addAcount(userName, "" + password));
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
					userID = (AccountsTable.executeQueryReturnUserID(userName));
					myChatFrame.setUser(userID, userName);
					myChatFrame.getSignInPanel().removeAll();
					myChatFrame.getSignInPanel().add(myChatFrame.getLblWelcome());
					myChatFrame.getLblWelcome().setText("Welcome " + userName + "!\nPlease choose a channel to chat in...");
				} // check if passwords match
				else if (actualPassword != password) { // This means there is a different password associated with this user name.
					JOptionPane.showMessageDialog(null, "You either entered a wrong password\n"
							+ "or this username is taken.");
				} // this is reached if passwords match
				else {
					userID = (AccountsTable.executeQueryReturnUserID(userName));
					myChatFrame.setUser(userID, userName);
					myChatFrame.getSignInPanel().removeAll();
					myChatFrame.getSignInPanel().add(myChatFrame.getLblWelcome());
					myChatFrame.getLblWelcome().setText("Welcome back " + userName + "!\nPlease choose a channel to chat in...");
				}
				myChatFrame.repaint();
			}
		});
		btnSignIn.setMaximumSize(new Dimension(200,70));
		btnSignIn.setAlignmentX(CENTER_ALIGNMENT);
	}

	/**
	 * Returns the user id as an int.
	 * @return
	 */
	public int getUserID() {
		return userID;
	}
}
