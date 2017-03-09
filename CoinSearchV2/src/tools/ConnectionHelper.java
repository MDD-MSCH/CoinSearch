package tools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public abstract class ConnectionHelper {
	protected Alert errorAlert = new Alert(AlertType.ERROR);
	protected Connection connection;
	protected PreparedStatement prepStatement;
	protected ResultSet resultset;
	protected ObservableList<String> conservationLevelsList = FXCollections.observableArrayList("GE", "G", "SG", "S", "S-SS", "SS", "SS-VZ", "VZ", "VZ-ST", "ST");
	
	protected void init(){
		initAlert();
		getConnection();
	}
	private void initAlert(){
		errorAlert.setTitle("Error Message");
		errorAlert.setHeaderText("Exception Alert");
	}
	
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
			errorAlert.setContentText("Problem by scrolling through the ResultSet: \n" + e.toString());
			errorAlert.show();
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
			errorAlert.setContentText("Problem by scrolling through the ResultSet: \n" + e.toString());
			errorAlert.show();
		}
		return false;
	}

	protected boolean oneForward() {
		try {
			if (!resultset.isClosed()) {

				if (resultset.next()) {
					return true;
				}
				if (resultset.isAfterLast()) {
					resultset.last();
				}
			} else {
				updateConnection();
				oneForward();
			}
		} catch (SQLException e) {
			errorAlert.setContentText("Problem by scrolling through the ResultSet: \n" + e.toString());
			errorAlert.show();
		}
		return false;
	}

	protected boolean oneBackward() {
		try {
			if (!resultset.isClosed()) {
				if (resultset.previous()) {
					return true;
				}
				if (resultset.isBeforeFirst()) {
					resultset.first();
				}
			} else {
				updateConnection();
				oneBackward();
			}
		} catch (SQLException e) {
			errorAlert.setContentText("Problem by scrolling through the ResultSet: \n" + e.toString());
			errorAlert.show();
		}
		return false;
	}
}