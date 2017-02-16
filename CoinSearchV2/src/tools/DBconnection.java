package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public enum DBconnection {
	INSTANCE;

	private Connection connection;
	private PreparedStatement prepStatement;
	private ResultSet resultset; 
	
	public Connection getConnection() {
		return this.connection;
	}
	
	public PreparedStatement getPrepStatement() {
		return prepStatement;
	}

	public ResultSet getResultset() {
		return this.resultset;
	}

	private DBconnection() {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		openDB();
		getResult("select * from coins.muenzen");
	}

	private void openDB() {
		connection = null;
		prepStatement = null;
		resultset = null;
		String arg = "jdbc:mariadb://localhost:3306/coins?";
		String userName = "root";
		String password = "102#3H9k8M4!5b";
		try {
			connection = DriverManager.getConnection(arg, userName, password);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Problem1: \n" + e.toString());
		}
		System.out.println(connection);
	}
	
	private void getResult(String query) {
		resultset = null;
		prepStatement = null;
		try {
			prepStatement = connection.prepareStatement(query);
			resultset = prepStatement.executeQuery();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Problem2: \n" + e.toString());
		}	
	}

	public void closeConnection() {
		try {
			if(resultset != null){
				resultset.close();
			}
			if(prepStatement != null){
				prepStatement.close();
			}
			if(connection != null){
				connection.close();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void refreshConnection(){
		closeConnection();
		openDB();
		getResult("select * from coins.muenzen");
	}
}
