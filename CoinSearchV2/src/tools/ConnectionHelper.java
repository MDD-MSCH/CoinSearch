package tools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ConnectionHelper {
	protected Connection connection;
	protected Statement statement;
	protected ResultSet resultset;
	
	protected void getConnection() {
		try {
			connection = DBconnection.INSTANCE.getConnection();
			resultset = DBconnection.INSTANCE.getResultset();
			statement = DBconnection.INSTANCE.getStatement();
		} catch (Exception e) {
				e.printStackTrace();
		}
	}
	
	protected void updateConnection() {
		DBconnection.INSTANCE.refreshConnection();
		connection = DBconnection.INSTANCE.getConnection();
		resultset = DBconnection.INSTANCE.getResultset();
	}
	
	protected boolean first(){
		try {
			if (!resultset.isClosed()) {
				resultset.first();
				return true;
			} else {
				updateConnection();
				first();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	protected boolean last(){
		try {
			if (!resultset.isClosed()) {
				resultset.last();
				return true;
			} else {
				updateConnection();
				last();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	protected boolean oneForward(){
		try {
			if (!resultset.isClosed()) {
				if (resultset.next())
					return true;
			} else {
				updateConnection();
				oneForward();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	protected boolean oneBackward(){
		try {
			if (!resultset.isClosed()) {
				if (resultset.previous())
					return true;
			} else {
				updateConnection();
				oneBackward();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
