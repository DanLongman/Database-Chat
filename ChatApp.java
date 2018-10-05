package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ChatApp {

	public static void main(String[] args) throws SQLException {
		//executeSqlStatement(ChannelTable.createTable(), ChannelTable.fillTable());
		//executeSqlStatement(AccountsTable.createTable(), AccountsTable.fillTable());
		//executeSqlStatement(MessagesTable.createTable(), MessagesTable.fillTable());
		
		//executeQueries(MessagesTable.query_All(), AccountsTable.query_NumOfUsers(), ChannelTable.query_All());
		//executeSqlStatement(AccountsTable.dropTable(), AccountsTable.createTable()); // accounts has been created
		//executeSqlStatement(MessagesTable.createTable());
		//executeSqlStatement(MessagesTable.dropTable(), MessagesTable.createTable());
		//executeSqlStatement(AccountsTable.dropTable(), AccountsTable.createTable());
		try(Connection connection = DriverManager.getConnection("jdbc:derby:ChatDatabase;create=true");
				Statement statement = connection.createStatement()) {
//			List<String> messages = MessagesTable.executeQueryReturnMessagesFromChannel(1);
//			System.out.println(messages);
//			System.out.println(AccountsTable.executeQueryReturnUsername(AccountsTable.query_UserByID(Integer.parseInt(messages.get(0)))));
			executeQueries(AccountsTable.query_All());
			System.out.println("done");
		}
	}
	
	/**
	 * Executes the sql statements in the order passed by the user.
	 * All sql statements that are passed as arguments change the database,
	 * but don't return any data.
	 * 
	 * @param sqlStatements
	 * @throws SQLException
	 */
	@SuppressWarnings("unused")
	public static void executeSqlStatement(String... sqlStatements) throws SQLException {
		try(Connection connection = DriverManager.getConnection("jdbc:derby:ChatDatabase;create=true");
				Statement statement = connection.createStatement()) {
			for(String sqlStatement : sqlStatements) {
				statement.execute(sqlStatement);
			}
		}
	}
	
	/**
	 * Executes all the queries in the order passed by the user. Each of
	 * those queries returns data and the method executeQueries displays
	 * that data.
	 * 
	 * @param sqlQueries
	 * @throws SQLException
	 */
	public static void executeQueries(String... sqlQueries) throws SQLException {
		try(Connection connection = DriverManager.getConnection("jdbc:derby:ChatDatabase;create=true");
				Statement statement = connection.createStatement()) {
			
			for(String sqlQuery : sqlQueries) {
				ResultSet results = statement.executeQuery( sqlQuery);
				printData(results);
				System.out.println();
			}
			
		}
	}
	
	@SuppressWarnings("unused")
	public static void printData(ResultSet results) throws SQLException {
			ResultSetMetaData metaData = results.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			printHeader(metaData);
			// print data
			while(results.next()) {
				for(int i = 1; i <= columnCount; i++) {
					System.out.printf("%-" + metaData.getColumnLabel(i).length() + "s  ",
							results.getObject(i));
				}
				System.out.println();
			}
	}
	
	public static void printHeader(ResultSetMetaData metaData)
			throws SQLException {
		int columnCount = metaData.getColumnCount();
		// print column headers
		for(int i = 1; i <= columnCount; i++) {
			System.out.print(metaData.getColumnLabel(i) + "  ");
		}
		System.out.println();
		
		// print dashed line
		for(int i = 1; i <= columnCount; i++) {
			for(int j = 0; j < metaData.getColumnLabel(i).length(); j++) {
				System.out.print('-');
			}
			if(i != columnCount) System.out.print("--");
		}
		System.out.println();
	}

	/**
	 * Returns the query as a ResultSet.
	 * @param sqlQuery
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet executeQueryReturnResultSet(String sqlQuery) throws SQLException {
		try(Connection connection = DriverManager.getConnection("jdbc:derby:ChatDatabase;create=true");
				Statement statement = connection.createStatement()) {
			ResultSet results = statement.executeQuery(sqlQuery);
			return results;
		}
	}
	
	/**
	 * Returns the ResultSet as a ResultSetMetaData
	 * @param sqlQuery
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetMetaData executeQueryReturnResultSetMetaData(String sqlQuery) throws SQLException {
		try(Connection connection = DriverManager.getConnection("jdbc:derby:ChatDatabase;create=true");
				Statement statement = connection.createStatement()) {
		
			ResultSet results = statement.executeQuery(sqlQuery);
			ResultSetMetaData metaData = results.getMetaData();
			return metaData;
		}
	}
}
