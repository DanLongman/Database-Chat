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
import java.util.ArrayList;
import java.util.List;

/**
 * Provides basic functionality (data insertions and queries) for the accounts table.
 * @author Joshua DeMoss & Daniel Longman
 *
 */
public class AccountsTable {

	/**
	 * Returns the String used to add an account to the table.
	 * @return
	 */
	public static String addAcount(String userName, String password) {
		return "INSERT INTO Accounts (UserName, Password) VALUES "
				+ "('" + userName + "', " + password + ")";
	}
	// - - - - - - - - - - - - - Query Statements - - - - - - - - - - - - - 
	
	/**
	 * Returns the String used to query all of the data from the accounts table.
	 * @return
	 */
	public static String query_All() {
		return "SELECT * FROM Accounts ";
	}

	/**
	 * Returns the String used to query the user names from the accounts table.
	 * @return
	 */
	public static String query_Users() {
		return "SELECT UserName FROM Accounts ";
	}

	/**
	 * Returns the String used to query the users by the specified userID.
	 * @return
	 */
	public static String query_UserByID(int userID) {
		return "SELECT UserName FROM Accounts "
				+ "WHERE UserID=" + userID;
	}

	/**
	 * Returns the String used to update the specified user's online time.
	 * @return
	 */
	public static String query_updateUserTime(int userID) {
		return "UPDATE Accounts SET OnlineTime = UNIX_TIMESTAMP() WHERE UserID=" + userID;
	}

	/**
	 * Returns the String used to query the users by the specified password.
	 * @return
	 */
	public static String query_UserByPassword(int password) {
		return "SELECT UserName FROM Accounts "
				+ "WHERE Password=" + password;
	}

	/**
	 * Returns the String used to query the password by the specified user name.
	 * @return
	 */
	public static String query_PasswordByUser(String username) {
		return "SELECT Password FROM Accounts "
				+ "WHERE UserName='" + username + "'";
	}

	/**
	 * Returns the String used to query the userID by the specified user name.
	 * @return
	 */
	public static String query_UserID(String userName) {
		return "SELECT UserID FROM Accounts "
				+ "WHERE UserName='" + userName + "'";
	}

	/**
	 * Returns the String used to query the number of users in the accounts table.
	 * @return
	 */
	public static String query_NumOfUsers() {
		return "SELECT COUNT(*) "
				+ "FROM Accounts";
	}

	/**
	 * Returns the String used to query the users that are currently considered online.
	 * @return
	 */
	public static String query_OnlineUsers() {
		return "SELECT UserName "
				+ "FROM Accounts "
				+ "WHERE OnlineTime > UNIX_TIMESTAMP() - 300";
	}

	/**
	 * Updates the online time for the specified user.
	 * @param userID
	 */
	public static void updateOnlineTime(int userID) {
		try (Connection connection = DriverManager.getConnection(
				"jdbc:mysql://73.228.84.50:3306/Chat?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "Lon^gm4n");
				Statement statement = connection.createStatement()) {
			statement.execute(AccountsTable.query_updateUserTime(userID));
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Returns the user ID.
	 *
	 * @param sqlQuery
	 * @return
	 * @throws SQLException
	 */
	public static int executeQueryReturnUserID(String username) {
		try (Connection connection = DriverManager.getConnection(
				"jdbc:mysql://73.228.84.50:3306/Chat?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "Lon^gm4n");
				Statement statement = connection.createStatement()) {
			ResultSet results = statement.executeQuery(AccountsTable.query_UserID(username));
			while (results.next()) {
				return (int) results.getObject(1);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Returns the list of online users.
	 * @return
	 */
	public static List<String> getOnlineUsers() {
		List<String> onlineUsers = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(
				"jdbc:mysql://73.228.84.50:3306/Chat?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "Lon^gm4n");
				Statement statement = connection.createStatement()) {
			ResultSet results = statement.executeQuery(AccountsTable.query_OnlineUsers());
			while (results.next()) {
				onlineUsers.add((String)results.getObject(1));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return onlineUsers;
	}

	/**
	 * Returns the specified user's password (selects password by username).
	 *
	 * @param sqlQuery
	 * @return
	 * @throws SQLException
	 */
	public static int executeQueryReturnPassword(String username) {
		try (Connection connection = DriverManager.getConnection(
				"jdbc:mysql://73.228.84.50:3306/Chat?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "Lon^gm4n");
				Statement statement = connection.createStatement()) {
			ResultSet results = statement.executeQuery(query_PasswordByUser(username));
			while (results.next()) {
				return (int) results.getObject(1);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	/**
	 * Returns the username as a String (according to ID).
	 *
	 * @param sqlQuery
	 * @return
	 * @throws SQLException
	 */
	public static String executeQueryReturnUsername(String sqlQuery) {
		try (Connection connection = DriverManager.getConnection(
				"jdbc:mysql://73.228.84.50:3306/Chat?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "Lon^gm4n");
				Statement statement = connection.createStatement()) {
			ResultSet results = statement.executeQuery(sqlQuery);
			while (results.next()) {
				return (String) results.getObject(1);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return "Anonymous";
	}
}
