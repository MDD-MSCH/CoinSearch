package controller.tab;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tools.DBconnection;

public class EditEntryTab {
	private Connection verbindung;
	private Statement statement;
	private ResultSet ergebnisMenge;
	private String query;

	@FXML
	private Label id, wertLabel, waehrungLabel, jahrLabel, inschriftKopfLabel, inschriftZahlLabel, zustandLabel,
			praegeortLabel;
	@FXML
	private TextField wert, waehrung, jahr, inschriftKopf, inschriftZahl, zustand, praegeort;

	@FXML
	public void initialize() {
		query = "SELECT * FROM coins.muenzen";
		getConnection();
	}

	private void getConnection() {
		try {
			verbindung = DBconnection.INSTANCE.getConnection();
			ergebnisMenge = DBconnection.INSTANCE.getResultset();
			statement = verbindung.createStatement();
			if (ergebnisMenge.next()) {
				datenLesen();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void updateConnection() {
		DBconnection.INSTANCE.refreshConnection();
		verbindung = DBconnection.INSTANCE.getConnection();
		ergebnisMenge = DBconnection.INSTANCE.getResultset();
	}

	private void datenLesen() {
		try {
			id.setText(Integer.toString(ergebnisMenge.getInt(1)));
			wert.setText(ergebnisMenge.getString(2));
			waehrung.setText(ergebnisMenge.getString(3));
			jahr.setText(Integer.toString(ergebnisMenge.getInt(4)));
			inschriftKopf.setText(ergebnisMenge.getString(5));
			inschriftZahl.setText(ergebnisMenge.getString(6));
			zustand.setText(ergebnisMenge.getString(7));
			praegeort.setText(ergebnisMenge.getString(8));
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Problem by reading: \n" + e.toString());
			e.printStackTrace();
		}
	}

	@FXML
	private void delete() {
		query = "DELETE FROM muenzen WHERE id = '" + id.getText() + "'";
		try {
			int position;
			position = ergebnisMenge.getRow();
			PreparedStatement prepStatement = verbindung.prepareStatement(query);
			prepStatement.executeQuery();
			DBconnection.INSTANCE.refreshConnection();
			getConnection();
			ergebnisMenge.absolute(position);
			if (ergebnisMenge.isAfterLast())
				ergebnisMenge.last();
			datenLesen();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Problem by deleting: \n" + e.toString());
		}
	}

	@FXML
	private void save() {
		query = "UPDATE muenzen SET wert = '" + wert.getText() + "', waehrung = '" + waehrung.getText() 
				+ "', jahr = '"+ jahr.getText() + "', inschriftKopf = '" + inschriftKopf.getText() 
				+ "', inschriftZahl = '"+ inschriftZahl.getText() + "', zustand = '" + zustand.getText() 
				+ "', praegeort = '"+ praegeort.getText() + "' WHERE id = '" + id.getText() + "'";
		try {
			int position;
			position = ergebnisMenge.getRow();
			PreparedStatement prepStatement = verbindung.prepareStatement(query);
			prepStatement.executeQuery();
			DBconnection.INSTANCE.refreshConnection();
			getConnection();
			ergebnisMenge.absolute(position);
			if (ergebnisMenge.isAfterLast())
				ergebnisMenge.last();
			datenLesen();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Problem by saving: \n" + e.toString());
		}
	}

	@FXML
	private void ganzVor() {
		try {
			if (!ergebnisMenge.isClosed()) {
				ergebnisMenge.first();
				datenLesen();
			} else {
				updateConnection();
				ganzVor();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void ganzZurueck() {
		try {
			if (!ergebnisMenge.isClosed()) {
				ergebnisMenge.last();
				datenLesen();
			} else {
				updateConnection();
				ganzZurueck();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void einenVor() {
		try {
			if (!ergebnisMenge.isClosed()) {
				if (ergebnisMenge.next())
					datenLesen();
			} else {
				updateConnection();
				einenVor();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void einenZurueck() {
		try {
			if (!ergebnisMenge.isClosed()) {
				if (ergebnisMenge.previous())
					datenLesen();
			} else {
				updateConnection();
				einenZurueck();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}