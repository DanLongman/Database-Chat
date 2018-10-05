package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountsTable {
	public static String createTable() {
		return
				"CREATE TABLE Accounts (" 
				+"UserID int not null primary key " 
				+"  GENERATED ALWAYS AS IDENTITY\n"
				+"  (START WITH 1, INCREMENT BY 1),"
				+ "UserName varchar(255),"
				+ "Password int)";
	}
	
	public static String fillTable() {
		return
				"INSERT INTO Accounts (UserName, Password) VALUES "
				+"('Josh', 9876),"
				+"('Daniel', 1099),"
				+"('Latisha', 2345)";
	}
	
	public static String addAcount(String userName, String password) { // TODO
		return
				"INSERT INTO Accounts (UserName, Password) VALUES "
				+"('" + userName + "', " + password + ")";
	}
	
	public static String dropTable() {
		return "DROP Table Accounts";
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
		return "SELECT COUNT(*) " +
				"FROM Accounts";
	}
	
	/**
	 * Returns the user ID.
	 * @param sqlQuery
	 * @return
	 * @throws SQLException
	 */
	public static int executeQueryReturnUserID(String username) throws SQLException {
		try(Connection connection = DriverManager.getConnection("jdbc:derby:ChatDatabase;create=true");
				Statement statement = connection.createStatement()) {
			ResultSet results = statement.executeQuery(AccountsTable.query_UserID(username));
			while(results.next())
				return (int)results.getObject(1);
			return 0;
		}
	}
	
	/**
	 * Returns the specified user's password (selects password by username).
	 * @param sqlQuery
	 * @return
	 * @throws SQLException
	 */
	public static int executeQueryReturnPassword(String username) throws SQLException {
		try(Connection connection = DriverManager.getConnection("jdbc:derby:ChatDatabase;create=true");
				Statement statement = connection.createStatement()) {
			ResultSet results = statement.executeQuery(query_PasswordByUser(username));
			while(results.next())
				return (int)results.getObject(1);
			return 0;
		}
	}
	
	/**
	 * Returns the username as a String (according to ID).
	 * @param sqlQuery
	 * @return
	 * @throws SQLException
	 */
	public static String executeQueryReturnUsername(String sqlQuery) throws SQLException {
		try(Connection connection = DriverManager.getConnection("jdbc:derby:ChatDatabase;create=true");
				Statement statement = connection.createStatement()) {
			ResultSet results = statement.executeQuery(sqlQuery);
			while(results.next())
				return (String)results.getObject(1);
			return "Anonymous";
		}
	}
	
	// ------------ test client --------------
	public static void main(String[] args) throws SQLException {
		//executeQueryReturnUserID(AccountsTable.query_UserID("Chantel"));
		//ChatApp.executeQueries(query_Users());
		System.out.println(executeQueryReturnUsername(AccountsTable.query_UserByID(1)));
	}
}
