package tools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class ConnectionHelper {
	protected Connection connection;
	protected PreparedStatement prepStatement;
	protected ResultSet resultset;

	protected void getConnection() {
		connection = DBconnection.INSTANCE.getConnection();
		prepStatement = DBconnection.INSTANCE.getPrepStatement();
		resultset = DBconnection.INSTANCE.getResultset();
	}

	protected void updateConnection() {
		DBconnection.INSTANCE.refreshConnection();
		getConnection();
	}

	protected boolean first() {
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

	protected boolean last() {
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

	protected boolean oneForward() {
		try {
			if (!resultset.isClosed()) {

				if (resultset.next()) {
					if (resultset.isBeforeFirst())
						resultset.first();
					return true;
				}
			} else {
				updateConnection();
				oneForward();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	protected boolean oneBackward() {
		try {
			if (!resultset.isClosed()) {
				if (resultset.previous()) {
					if (resultset.isBeforeFirst())
						resultset.first();
					System.out.println(resultset.getRow());
					return true;
				}
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