package controller.tab;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tools.ConnectionHelper;
import tools.DBconnection;
import tools.LetterTextField;
import tools.NumberTextField;
import tools.checkTextField;

public class NewEntryTab extends ConnectionHelper {
	private checkTextField check;
	
	@FXML
	private Label id, wertLabel, waehrungLabel, jahrLabel, inschriftKopfLabel, inschriftZahlLabel, zustandLabel,
			praegeortLabel, hintLabel;
	@FXML
	private TextField wert, inschriftKopf, inschriftZahl, praegeort;

	@FXML 
	private NumberTextField jahr;
	
	@FXML 
	private LetterTextField waehrung;
	
	@FXML
	private ComboBox<String> zustand;

	@FXML
	public void initialize() {
		zustand.setItems(conservationLevelsList);
		init();
	}

	@FXML
	private void uebernehmen() {
		check = new checkTextField();
		List<String> inputList = Arrays.asList(wert.getText(), waehrung.getText(), jahr.getText(),
				inschriftKopf.getText(), inschriftZahl.getText(), zustand.getValue(), praegeort.getText());
		if (inputList != null) {
			if (check.checkIfStringIsNotEmpty(inputList) == inputList.size() && checkInput()) {
				try {
					String query = " insert into muenzen (wert, waehrung, jahr, inschriftKopf, inschriftZahl, zustand, praegeort)"
							+ " values (?, ?, ?, ?, ?, ?, ?)";
					prepStatement = connection.prepareStatement(query);
					// prepStatement.setInt(1, idkey);
					prepStatement.setString(1, wert.getText());
					prepStatement.setString(2, waehrung.getText());
					prepStatement.setString(3, jahr.getText());
					prepStatement.setString(4, inschriftKopf.getText());
					prepStatement.setString(5, inschriftZahl.getText());
					prepStatement.setString(6, zustand.getValue());
					prepStatement.setString(7, praegeort.getText());
					prepStatement.executeQuery();
					cleanTextFields();
					DBconnection.INSTANCE.refreshConnection();
				} catch (SQLException e) {
					errorAlert.setContentText("Problem by Saving: \n" + e.toString());
					errorAlert.show();
				}
			} else {
				hintLabel.setText("Please fill up all fields correctly.");
			}
		} else {
			hintLabel.setText("Please fill up all fields correctly.");
		}
	}

	private boolean checkInput() {
		boolean readyForExecute = false;
		if (check.onlyNumbers(jahr.getText()) && check.onlyLetters(praegeort.getText())) {
			readyForExecute = true;
		}
		return readyForExecute;
	}

	private void cleanTextFields() {
		wert.setText(null);
		waehrung.setText(null);
		jahr.setText(null);
		inschriftKopf.setText(null);
		inschriftZahl.setText(null);
		// zustand.setText(null);
		praegeort.setText(null);
		hintLabel.setText(null);
	}

	@FXML
	private void exit() {
		DBconnection.INSTANCE.closeConnection();
		System.exit(0);
	}
}