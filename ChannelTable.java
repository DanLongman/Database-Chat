package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ChannelTable {
	public static String createTable() {
		return
				"CREATE TABLE Channels (" 
				+"ChannelID int not null primary key " 
				+"  GENERATED ALWAYS AS IDENTITY\n"
				+"  (START WITH 1, INCREMENT BY 1),"
				+ "Name varchar(255))";
	}
	
	public static String fillTable() {
		return
				"INSERT INTO Channels (Name) VALUES "
				+"('General'),"
				+"('Gaming'),"
				+"('Inspirational')";
	}
	
	public static String dropTable() {
		return "DROP Table Channels";
	}
	
	// - - - - - - - - - - - - - Query Statements - - - - - - - - - - - - - 
	
	public static String query_All() {
		return "SELECT * FROM Channels ";
	}
	
	public static String query_Name(int channelID) {
		return "SELECT Name FROM Channels "
				+ "WHERE channelID=" + channelID;
	}
	
	public static String query_numOfChannels() {
		return "SELECT COUNT(ChannelID) "
				+"FROM Channels";
	}
	
	/**
	 * Returns the user number of channels in the database as an int.
	 * @param sqlQuery
	 * @return
	 * @throws SQLException
	 */
	public static int executeQueryReturnNumOfChannels() throws SQLException {
		try(Connection connection = DriverManager.getConnection("jdbc:derby:ChatDatabase;create=true");
				Statement statement = connection.createStatement()) {
			ResultSet results = statement.executeQuery(query_numOfChannels());
			while(results.next())
				return (int)results.getObject(1);
			return 0;
		}
	}
	
	/**
	 * Returns the user number of channels in the database as an int.
	 * @param sqlQuery
	 * @return
	 * @throws SQLException
	 */
	public static String executeQueryReturnChannelName(int channelID) throws SQLException {
		try(Connection connection = DriverManager.getConnection("jdbc:derby:ChatDatabase;create=true");
				Statement statement = connection.createStatement()) {
			ResultSet results = statement.executeQuery(query_Name(channelID));
			while(results.next())
				return (String)results.getObject(1);
			return null;
		}
	}
}
