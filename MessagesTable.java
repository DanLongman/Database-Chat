package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MessagesTable {
		public static String createTable() {
			return
					"CREATE TABLE Messages (" 
					+"ID int not null primary key " 
					+"  GENERATED ALWAYS AS IDENTITY\n"
					+"  (START WITH 1, INCREMENT BY 1),"
					+ "Message varchar(255),"
					+ "UserID int,"
					+ "ChannelID int,"
					+ "Color varchar(255))";
		}
		
		public static String fillTable() {
			return
					"INSERT INTO Messages (Message, UserID, ChannelID, Color) VALUES "
					+"('Hello', 1, 2, 'red'),"
					+"('Hey you', 2, 2, 'black'),"
					+"('Call me', 1, 1, 'red')";
		}
		
		public static String addMessageToTable(String message, String userID, String channelID, String color) { 
			return
					"INSERT INTO Messages (Message, UserID, ChannelID, Color) VALUES "
					+"('" + message + "', " + userID + ", "+ channelID + ", " + "'" + color + "')";
		}
		
		public static String updateColor(String userID, String color) { 
			return
					"UPDATE Customers " 
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
			return "SELECT UserID, Message FROM Messages "
			+ "WHERE ChannelID=" + channelID;
		}
		
		/**
		 * Returns all of the messages in the chosen channel.
		 * @param sqlQuery
		 * @return
		 * @throws SQLException
		 */
		public static List<String> executeQueryReturnMessagesFromChannel(int channelID) throws SQLException {
			try(Connection connection = DriverManager.getConnection("jdbc:derby:ChatDatabase;create=true");
					Statement statement = connection.createStatement()) {
				ResultSet results = statement.executeQuery(query_MessagesFromChannel(channelID));
			
				List<String> messages = new ArrayList<>();
				while(results.next()) {
					for(int i = 1; i <= 2; i++)
						if(i == 1)
							messages.add(Integer.toString((int)results.getObject(i)));
						else
							messages.add((String)results.getObject(i));
				}
				return messages;
			}
		}
}