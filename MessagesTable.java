package a2chat;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessagesTable {

	public static String createTable() {
		return "CREATE TABLE Messages ("
				+ "ID  int not null primary key AUTO_INCREMENT,"
				+ "Message varchar(255),"
				+ "UserID int,"
				+ "ChannelID int,"
				+ "Color varchar(255))";
	}

	public static String fillTable() {
		return "INSERT INTO Messages (Message, UserID, ChannelID, Color) VALUES "
				+ "('Hello', 1, 2, 'red'),"
				+ "('Hey you', 2, 2, 'black'),"
				+ "('Call me', 1, 1, 'red')";
	}

	public static String addMessageToTable(String message, int userID, int channelID, String color) {
		return "INSERT INTO Messages (Message, UserID, ChannelID, Color) VALUES "
				+ "('" + message + "', " + userID + ", " + channelID + ", " + "'" + color + "')";
	}

	public static String updateColor(String userID, String color) {
		return "UPDATE Customers "
				+ "SET Color = '" + color + "' "
				+ "WHERE UserID = " + userID;
	}

	public static String dropTable() {
		return "DROP Table Messages";
	}

	// - - - - - - - - - - - - - Query Statements - - - - - - - - - - - - - 
	public static String query_All() {
		return "SELECT * FROM Messages ";
	}

	public static String query_MessagesFromChannel(int channelID) {
		return "SELECT UserName, Message, Color FROM Messages "
				+ "JOIN Accounts on Accounts.UserID = Messages.UserID "
				+ "WHERE ChannelID=" + channelID;
	}

	public static List<Message> getMessagesFromChannel(int channelID) {
		List<Message> messages = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(
				"jdbc:mysql://24.11.122.199:3306/Chat?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "Lon^gm4n");
				Statement statement = connection.createStatement()) {
			ResultSet results = statement.executeQuery(query_MessagesFromChannel(channelID));

			while (results.next()) {
				Message m = new Message();
				m.user = (String) results.getObject(1);
				m.messageText = (String) results.getObject(2);
				String colorString = (String) results.getObject(3);
				switch (colorString) {
					case "blue":
						m.color = Color.BLUE;
						break;
					case "pink":
						m.color = Color.PINK;
						break;
					case "green":
						m.color = Color.GREEN;
						break;
					case "yellow":
						m.color = Color.YELLOW;
						break;
					case "red":
						m.color = Color.RED;
						break;
					case "orange":
						m.color = Color.ORANGE;
						break;
					case "cyan":
						m.color = Color.CYAN;
						break;
					case "black":
						m.color = Color.BLACK;
						break;
					case "magenta":
						m.color = Color.MAGENTA;
						break;
					default:
						m.color = Color.BLACK;
				}
				messages.add(m);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return messages;
	}

	/**
	 * Returns all of the messages in the chosen channel.
	 *
	 * @param channelID
	 * @return
	 */
	public static List<String> executeQueryReturnMessagesFromChannel(int channelID) {
		List<String> messages = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(
				"jdbc:mysql://24.11.122.199:3306/Chat?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "Lon^gm4n");
				Statement statement = connection.createStatement()) {
			ResultSet results = statement.executeQuery(query_MessagesFromChannel(channelID));

			while (results.next()) {
				for (int i = 1; i <= 2; i++) {
					if (i == 1) {
						messages.add(Integer.toString((int) results.getObject(i)));
					} else {
						messages.add((String) results.getObject(i));
					}
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return messages;
	}
}
