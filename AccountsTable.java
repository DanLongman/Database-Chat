package a2chat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AccountsTable {

//	Accounts ("
//		UserID  int
//		UserName varchar(255)
//		OnlineTime int
//		Password int

	public static String addAcount(String userName, String password) {
		return "INSERT INTO Accounts (UserName, Password) VALUES "
				+ "('" + userName + "', " + password + ")";
	}
	// - - - - - - - - - - - - - Query Statements - - - - - - - - - - - - - 
	public static String query_All() {
		return "SELECT * FROM Accounts ";
	}

	public static String query_Users() {
		return "SELECT UserName FROM Accounts ";
	}

	public static String query_UserByID(int userID) {
		return "SELECT UserName FROM Accounts "
				+ "WHERE UserID=" + userID;
	}

	public static String query_updateUserTime(int userID) {
		return "UPDATE Accounts SET OnlineTime = UNIX_TIMESTAMP() WHERE UserID=" + userID;
	}

	public static String query_UserByPassword(int password) {
		return "SELECT UserName FROM Accounts "
				+ "WHERE Password=" + password;
	}

	public static String query_PasswordByUser(String username) {
		return "SELECT Password FROM Accounts "
				+ "WHERE UserName='" + username + "'";
	}

	public static String query_UserID(String userName) {
		return "SELECT UserID FROM Accounts "
				+ "WHERE UserName='" + userName + "'";
	}

	public static String query_NumOfUsers() {
		return "SELECT COUNT(*) "
				+ "FROM Accounts";
	}

	public static String query_OnlineUsers() {
		return "SELECT UserName "
				+ "FROM Accounts "
				+ "WHERE OnlineTime > UNIX_TIMESTAMP() - 300";
	}

	public static void updateOnlineTime(int userID) {
		try (Connection connection = DriverManager.getConnection(
				"jdbc:mysql://24.11.122.199:3306/Chat?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "Lon^gm4n");
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
				"jdbc:mysql://24.11.122.199:3306/Chat?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "Lon^gm4n");
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
	
	public static List<String> getOnlineUsers() {
		List<String> onlineUsers = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(
				"jdbc:mysql://24.11.122.199:3306/Chat?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "Lon^gm4n");
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
				"jdbc:mysql://24.11.122.199:3306/Chat?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "Lon^gm4n");
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
				"jdbc:mysql://24.11.122.199:3306/Chat?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "Lon^gm4n");
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
