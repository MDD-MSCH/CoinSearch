package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Statement;

import javax.swing.JOptionPane;

public enum DBconnection {
	INSTANCE;

	private Connection connection; // verbindung
//	private static Statement statement; // statement
	private ResultSet resultset; // ergebnisMenge

	public Connection getConnection() {
		return connection;
	}

	public ResultSet getResultset() {
		return resultset;
	}

	private DBconnection() {
		oeffneDB();
		liefereErgebnis("select * from coins.muenzen");
	}

	private void oeffneDB() {
		String arg = "jdbc:mariadb://localhost:3306/coins?";
		String userName = "root";
		String password = "102#3H9k8M4!5b";
		connection = null;
		try {
			connection = DriverManager.getConnection(arg, userName, password);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Problem1: \n" + e.toString());
		}
		System.out.println(connection);
	}

	private void liefereErgebnis(String sqlAnweisung) {
		resultset = null;
//		statement = null;
		PreparedStatement prestmt = null;
		try {
//			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			prestmt = connection.prepareStatement(sqlAnweisung);
			resultset = prestmt.executeQuery();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Problem2: \n" + e.toString());
		}
		
	}

	public void closeConnection() {
		try {
			resultset.close();
			connection.close();
			System.exit(0);
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}
	
	public void refreshConnection(){
		try {
			resultset.close();
			connection.close();
			oeffneDB();
			liefereErgebnis("select * from coins.muenzen");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
