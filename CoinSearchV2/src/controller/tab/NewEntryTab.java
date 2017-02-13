package controller.tab;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;
import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tools.DBconnection;
import tools.checkTextField;

public class NewEntryTab {
	private Connection verbindung;
	private Statement statement;
	private ResultSet ergebnisMenge;
	private PreparedStatement prepStatement;
	private checkTextField check;

	@FXML
	private Label id, wertLabel, waehrungLabel, jahrLabel, inschriftKopfLabel, inschriftZahlLabel, zustandLabel,
			praegeortLabel, hintLabel;
	@FXML
	private TextField wert, waehrung, jahr, inschriftKopf, inschriftZahl, zustand, praegeort;

	private void getConnection() {
		verbindung = DBconnection.INSTANCE.getConnection();
		ergebnisMenge = DBconnection.INSTANCE.getResultset();
		statement = null;
		prepStatement = null;
	}

	@FXML
	private void uebernehmen() {
		check = new checkTextField();
		List<String> eingabe = Arrays.asList(wert.getText(), waehrung.getText(), jahr.getText(),
				inschriftKopf.getText(), inschriftZahl.getText(), zustand.getText(), praegeort.getText());
		if (eingabe != null) {
			if (check.checkIfStringIsNotEmpty(eingabe) == eingabe.size() && checkEingabe()) {

				try {
					getConnection();
					// findLastIdkey();
					String query = " insert into muenzen (wert, waehrung, jahr, inschriftKopf, inschriftZahl, zustand, praegeort)"
							+ " values (?, ?, ?, ?, ?, ?, ?)";
					prepStatement = verbindung.prepareStatement(query);
					// prepStatement.setInt(1, idkey);
					prepStatement.setString(1, wert.getText());
					prepStatement.setString(2, waehrung.getText());
					prepStatement.setString(3, jahr.getText());
					prepStatement.setString(4, inschriftKopf.getText());
					prepStatement.setString(5, inschriftZahl.getText());
					prepStatement.setString(6, zustand.getText());
					prepStatement.setString(7, praegeort.getText());
					prepStatement.executeQuery();
					cleanTextFields();
					DBconnection.INSTANCE.refreshConnection();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Problem by Saving: \n" + e.toString());
					e.printStackTrace();
				}
			} else {
				System.out.println("Please fill all fields correctly");
			}
		} else {
			System.out.println("Please fill all fields correctly");
		}
	}

	private boolean checkEingabe() {
		boolean readyForExecute = false;

		if (check.onlyNumbers(jahr.getText()) == false) {
			jahr.setText("Only Numbers Please");
		}
		if (check.onlyLetters(praegeort.getText()) == false) {
			praegeort.setText("Only Letters Please");
		}
		if (check.onlyNumbers(jahr.getText()) == true && check.onlyLetters(praegeort.getText()) == true) {
			readyForExecute = true;
		}
		return readyForExecute;
	}

	// private void findLastIdkey() {
	// try {
	// statement = verbindung.createStatement();
	// ergebnisMenge = statement.executeQuery("select max(id) as last_id from
	// muenzen;");
	// // lastid = ergebnisMenge.getString("last_id");
	// int idkey = ergebnisMenge.findColumn("last_id") + 3;
	// } catch (SQLException e) {
	// JOptionPane.showMessageDialog(null, "Problem to find last IDkey: \n" +
	// e.toString());
	// }
	// }

	private void cleanTextFields() {
		wert.setText(null);
		waehrung.setText(null);
		jahr.setText(null);
		inschriftKopf.setText(null);
		inschriftZahl.setText(null);
		zustand.setText(null);
		praegeort.setText(null);
	}

	@FXML
	private void exit() {
		DBconnection.INSTANCE.closeConnection();
	}
}