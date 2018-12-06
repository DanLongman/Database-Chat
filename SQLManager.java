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
 * Provides methods with basic sql database functionality.
 * @author Joshua DeMoss & Daniel Longman
 *
 */
public class SQLManager {

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Executes the sql statements in the order passed by the user. All sql statements that are passed as arguments change the database,
	 * but don't return any data.
	 *
	 * @param sqlStatements
	 * @throws SQLException
	 */
	@SuppressWarnings("unused")
	public static void executeSqlStatement(String... sqlStatements) throws SQLException {
		try (Connection connection = DriverManager.getConnection(
				"jdbc:mysql://73.228.84.50:3306/Chat?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "Lon^gm4n");
				Statement statement = connection.createStatement()) {
			for (String sqlStatement : sqlStatements) {
				statement.execute(sqlStatement);
			}
		}
	}

	/**
	 * Returns the query as a ResultSet.
	 *
	 * @param sqlQuery
	 * @return
	 * @throws SQLException
	 */
	public static ResultSet executeQueryReturnResultSet(String sqlQuery) throws SQLException {
		try (Connection connection = DriverManager.getConnection(
				"jdbc:mysql://73.228.84.50:3306/Chat?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "Lon^gm4n");
				Statement statement = connection.createStatement()) {
			ResultSet results = statement.executeQuery(sqlQuery);
			return results;
		}
	}
}
