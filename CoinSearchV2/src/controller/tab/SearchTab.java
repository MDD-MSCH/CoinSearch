package controller.tab;

import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import tools.ConnectionHelper;

public class SearchTab extends ConnectionHelper {
	private static String url, wertLabel, wertWaehrung, wertJahr, wertInschriftKopf, wertInschriftZahl, wertZustand, wertPraegeort;
	private byte countValues;
	private boolean urlSelected;
	private Tab1Controller tc = null;
	private ObservableList<String> list = FXCollections.observableArrayList("muenzkatalog-online.de", "baldwin.co.uk", "coins.ha.com", "mdm.de");

	@FXML
	private Label id, wert, waehrung, jahr, inschriftKopf, inschriftZahl, zustand, praegeort, hint;

	@FXML
	private ComboBox<String> chooseURL;

	@FXML
	private CheckBox checkWert, checkWaehrung, checkJahr, checkInschriftKopf, checkInschriftZahl, checkZustand,
			checkPraegeort;

	@FXML
	private ProgressBar progressbar;
	
	@FXML
	public void initialize() {
		chooseURL.setItems(list);
		
		getConnection();
		try {
			if (resultset.next()) {
				setData();
			}
		} catch (SQLException e) {
			errorAlert.setContentText("Problem by initialising: \n" + e.toString());
			errorAlert.show();
		}
	}
	
	@FXML
	private void ganzVor() {
		if (first()) {
			setData();
		}
	}

	@FXML
	private void ganzZurueck() {
		if (last()) {
			setData();
		}
	}

	@FXML
	private void einenVor() {
		if (oneForward()) {
			setData();
		}
	}

	@FXML
	private void einenZurueck() {
		if (oneBackward()) {
			setData();
		}
	}

	@FXML
	private void startSearching(ActionEvent event) {
		urlSelected = false;
		countValues = 0;
		try {
			checkValues();
			if (countValues >= 1 && urlSelected == true) {
				Thread workerThread = new Thread(tc = new Tab1Controller());
				workerThread.start();
				
		//		progressbar.progressProperty().bind(tc.progress.progressProperty());
			} 
			if (countValues == 0 && urlSelected == true) {
				hint.setText("Please choose a value");
			}
			if (countValues == 0 && urlSelected == false) {
				hint.setText("Please choose a website and at least one value");
			}
			if (countValues > 0 && urlSelected == false) {
				hint.setText("Please choose a website");
			}
		} catch (java.lang.RuntimeException e) {
			errorAlert.setContentText("Problem by start searching: \n" + e.toString());
			errorAlert.show();
			e.printStackTrace();
		}
	}
	
	public static String getUrl() {
		String value = url;
		return value;
	}

	public static String getCheckWert() {
		String value = wertLabel;
		return value;
	}

	public static String getCheckWaehrung() {
		String value = wertWaehrung;
		return value;
	}

	public static String getCheckJahr() {
		String value = wertJahr;
		return value;
	}

	public static String getCheckInschriftKopf() {
		String value = wertInschriftKopf;
		return value;
	}

	public static String getCheckInschriftZahl() {
		String value = wertInschriftZahl;
		return value;
	}

	public static String getCheckZustand() {
		String value = wertZustand;
		return value;
	}

	public static String getCheckPraegeort() {
		String value = wertPraegeort;
		return value;
	}

	private void setData() {
		try {
			id.setText(Integer.toString(resultset.getInt(1)));
			wert.setText(resultset.getString(2));
			waehrung.setText(resultset.getString(3));
			jahr.setText(Integer.toString(resultset.getInt(4)));
			inschriftKopf.setText(resultset.getString(5));
			inschriftZahl.setText(resultset.getString(6));
			zustand.setText(resultset.getString(7));
			praegeort.setText(resultset.getString(8));
		} catch (Exception e) {
			errorAlert.setContentText("Problem by setting data: \n" + e.toString());
			errorAlert.show();
		}
	}

	private void checkValues() {
		try {
			checkWert();
			checkWaehrung();
			checkJahr();
			checkInschriftKopf();
			checkInschriftZahl();
			checkZustand();
			checkPraegeort();
			myURL();
		} catch (SQLException e) {
			errorAlert.setContentText("Problem by checking values: \n" + e.toString());
			errorAlert.show();
		}
	}

	private void checkWert() throws SQLException {
		if (checkWert.isSelected() == true) {
			wertLabel = resultset.getString(2);
			countValues++;
		} else
			wertLabel = null;
	}

	private void checkWaehrung() throws SQLException {
		if (checkWaehrung.isSelected() == true) {
			wertWaehrung = resultset.getString(3);
			countValues++;
		} else
			wertWaehrung = null;
	}

	private void checkJahr() throws SQLException {
		if (checkJahr.isSelected() == true) {
			wertJahr = resultset.getString(4);
			countValues++;
		} else
			wertJahr = null;
	}

	private void checkInschriftKopf() throws SQLException {
		if (checkInschriftKopf.isSelected() == true) {
			wertInschriftKopf = resultset.getString(5);
			countValues++;
		} else
			wertInschriftKopf = null;
	}

	private void checkInschriftZahl() throws SQLException {
		if (checkInschriftZahl.isSelected() == true) {
			wertInschriftZahl = resultset.getString(6);
			countValues++;
		} else
			wertInschriftZahl = null;
	}

	private void checkZustand() throws SQLException {
		if (checkZustand.isSelected() == true) {
			wertZustand = resultset.getString(7);
			countValues++;
		} else
			wertZustand = null;
	}

	private void checkPraegeort() throws SQLException {
		if (checkPraegeort.isSelected() == true) {
			wertPraegeort = resultset.getString(8);
			countValues++;
		} else
			wertPraegeort = null;
	}

	private void myURL() {
		if (chooseURL.getValue().equals("muenzkatalog-online.de")){
			url = "muenzkatalog-online.de";
			urlSelected = true;
		}
		if (chooseURL.getValue().equals("baldwin.co.uk")) {
			url = "baldwin.co.uk";
			urlSelected = true;
		}
		if (chooseURL.getValue().equals("coins.ha.com")) {
			url = "coins.ha.com";
			urlSelected = true;
		} 
	}
}