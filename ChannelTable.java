package database;

/******************************************
 * CSIS2410: Advanced Programming
 * Assignment 02: Database Application
 * Authors: Joshua DeMoss & Daniel Longman
 * Date: 10/07/2018
 ******************************************/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Provides basic functionality (data insertions and queries) for the channel table.
 * @author Joshua DeMoss & Daniel Longman
 *
 */
public class ChannelTable {
	// - - - - - - - - - - - - - Query Statements - - - - - - - - - - - - - 
	/**
	 * Returns a String used to query all channels from the table.
	 * @return
	 */
	public static String query_All() {
		return "SELECT * FROM Channels ";
	}

	/**
	 * Returns the String used to query the name of the channel according to the specified ID.
	 * @return
	 */
	public static String query_Name(int channelID) {
		return "SELECT Name FROM Channels "
				+ "WHERE channelID=" + channelID;
	}

	/**
	 * Returns the String used to query the number of channels.
	 * @return
	 */
	public static String query_numOfChannels() {
		return "SELECT COUNT(ChannelID) "
				+ "FROM Channels";
	}

	/**
	 * Returns the user number of channels in the database as an int.
	 *
	 * @param sqlQuery
	 * @return
	 * @throws SQLException
	 */
	public static int numOfChannels() {
		try (Connection connection = DriverManager.getConnection(
				"jdbc:mysql://24.11.122.199:3306/Chat?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "Lon^gm4n");
				Statement statement = connection.createStatement()) {
			ResultSet results = statement.executeQuery(query_numOfChannels());
			while (results.next()) {
				return Math.toIntExact((Long) results.getObject(1));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	/**
	 * Returns the user number of channels in the database as an int.
	 *
	 * @param sqlQuery
	 * @return
	 * @throws SQLException
	 */
	public static String getChannelName(int channelID) {
		try (Connection connection = DriverManager.getConnection(
				"jdbc:mysql://24.11.122.199:3306/Chat?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "Lon^gm4n");
				Statement statement = connection.createStatement()) {
			ResultSet results = statement.executeQuery(query_Name(channelID));
			while (results.next()) {
				return (String) results.getObject(1);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
