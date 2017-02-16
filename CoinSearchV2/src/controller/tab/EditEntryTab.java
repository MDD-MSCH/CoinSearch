package controller.tab;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tools.ConnectionHelper;
import tools.DBconnection;

public class EditEntryTab extends ConnectionHelper {
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
		try {
			if (resultset.next()) {
				readData();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void readData() {
		try {
			id.setText(Integer.toString(resultset.getInt(1)));
			wert.setText(resultset.getString(2));
			waehrung.setText(resultset.getString(3));
			jahr.setText(Integer.toString(resultset.getInt(4)));
			inschriftKopf.setText(resultset.getString(5));
			inschriftZahl.setText(resultset.getString(6));
			zustand.setText(resultset.getString(7));
			praegeort.setText(resultset.getString(8));
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Problem by reading: \n" + e.toString());
			e.printStackTrace();
		}
	}

	@FXML
	private void save() {
		query = "UPDATE muenzen SET wert = '" + wert.getText() + "', waehrung = '" + waehrung.getText() + "', jahr = '"
				+ jahr.getText() + "', inschriftKopf = '" + inschriftKopf.getText() + "', inschriftZahl = '"
				+ inschriftZahl.getText() + "', zustand = '" + zustand.getText() + "', praegeort = '"
				+ praegeort.getText() + "' WHERE id = '" + id.getText() + "'";
		try {
			int position;
			position = resultset.getRow();
			PreparedStatement prepStatement = connection.prepareStatement(query);
			prepStatement.executeQuery();
			DBconnection.INSTANCE.refreshConnection();
			getConnection();
			resultset.absolute(position);
			if (resultset.isAfterLast())
				resultset.last();
			readData();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Problem by saving: \n" + e.toString());
		}
	}

	@FXML
	private void delete() {
		query = "DELETE FROM muenzen WHERE id = '" + id.getText() + "'";
		try {
			int position;
			position = resultset.getRow();
			PreparedStatement prepStatement = connection.prepareStatement(query);
			prepStatement.executeQuery();
			DBconnection.INSTANCE.refreshConnection();
			getConnection();
			resultset.absolute(position);
			if (resultset.isAfterLast())
				resultset.last();
			readData();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Problem by deleting: \n" + e.toString());
		}
	}

	@FXML
	private void ganzVor() {
		if (first()) {
			readData();
		}
	}

	@FXML
	private void ganzZurueck() {
		if (last()) {
			readData();
		}
	}

	@FXML
	private void einenVor() {
		if (oneForward()) {
			readData();
		}

	}

	@FXML
	private void einenZurueck() {
		if (oneBackward()) {
			readData();
		}
	}
}