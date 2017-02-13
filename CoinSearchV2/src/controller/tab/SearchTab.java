package controller.tab;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import tools.DBconnection;


public class SearchTab {
	private static final long serialVersionUID = -5140210296788483971L;
	private static String url, wertLabel, wertWaehrung, wertJahr, wertInschriftKopf, wertInschriftZahl, wertZustand, wertPraegeort;
	
	private Connection verbindung;
	private ResultSet ergebnisMenge;
	private byte countValues;
	private boolean urlSelected;
	private String query;
	//private Stage stage;
	
	
	ObservableList<String> list = FXCollections.observableArrayList("baldwin.co.uk","coins.ha.com","mdm.de");
	
	@FXML
	private Label id, wert, waehrung, jahr, inschriftKopf, inschriftZahl, zustand, praegeort, hint;
	
	@FXML
	private ComboBox<String> chooseURL;
	
	@FXML
	private CheckBox checkWert, checkWaehrung, checkJahr, checkInschriftKopf, checkInschriftZahl, checkZustand, checkPraegeort;
	
	
	@FXML 
	public void initialize() {
		chooseURL.setItems(list);
		createConnection();
	}
	
	
	private void createConnection() {
		query = "SELECT * FROM coins.muenzen";
		try {
			verbindung = DBconnection.INSTANCE.getConnection();
			ergebnisMenge = DBconnection.INSTANCE.getResultset();
			if (ergebnisMenge.next()) {
					datenLesen();
			}
			
		} catch (Exception e) {
				e.printStackTrace();
		}
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
		}
		catch(Exception e) {
			//JOptionPane.showMessageDialog(this, "Problem: \n" + e.toString());
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
	
	private void updateConnection() {
		DBconnection.INSTANCE.refreshConnection();
		verbindung = DBconnection.INSTANCE.getConnection();
		ergebnisMenge = DBconnection.INSTANCE.getResultset();
	}

	@FXML
	private void startSearching(ActionEvent event) throws SQLException{
		urlSelected = false;
		countValues = 0;
		try {
			checkValues();
			if (countValues >= 1 && urlSelected == true){
			Thread workerThread = new Thread(new Tab1Controller());
			workerThread.start();
			}else if(countValues == 0 && urlSelected == true){
				hint.setText("Please choose a value");
			}
		} catch ( java.lang.RuntimeException e) {
			if (countValues == 0 && urlSelected == false){
			hint.setText("Please choose a website and at least one value");
			}else if(countValues >0 && urlSelected == false){
				hint.setText("Please choose a website");
			}
		}
	}
	
	private void checkValues() throws SQLException{
		checkWert();
		checkWaehrung();
		checkJahr();
		checkInschriftKopf();
		checkInschriftZahl();
		checkZustand();
		checkPraegeort();
		myURL();
	}
	
	private void checkWert() throws SQLException{
		if(checkWert.isSelected()==true){
			wertLabel = ergebnisMenge.getString(2);
			countValues++;
		}else 
			wertLabel = null;		
	}
	private void checkWaehrung() throws SQLException{
		if(checkWaehrung.isSelected()==true){
			wertWaehrung = ergebnisMenge.getString(3);
			countValues++;
		}else
			wertWaehrung = null;
	}
	private void checkJahr() throws SQLException{
		if( checkJahr.isSelected()==true){
			wertJahr = ergebnisMenge.getString(4);
			countValues++;
		}else
			wertJahr = null;
	}
	private void checkInschriftKopf() throws SQLException{
		if(checkInschriftKopf.isSelected()==true){
			wertInschriftKopf = ergebnisMenge.getString(5);
			countValues++;
		}else
			wertInschriftKopf = null;
	}
	private void checkInschriftZahl() throws SQLException{
		if(checkInschriftZahl.isSelected()==true){
			wertInschriftZahl = ergebnisMenge.getString(6);
			countValues++;
		}else
			wertInschriftZahl = null;
	}
	private void checkZustand() throws SQLException{
		if(checkZustand.isSelected()==true){
			wertZustand = ergebnisMenge.getString(7);
			countValues++;
		}else
			wertZustand = null;
	}
	private void checkPraegeort() throws SQLException{
		if(checkPraegeort.isSelected()==true){
			wertPraegeort = ergebnisMenge.getString(8);
			countValues++;
		}else
			wertPraegeort = null;
	}
	private void myURL(){
		if (chooseURL.getValue().equals("baldwin.co.uk")){
			url = "baldwin.co.uk";
			urlSelected = true;
		}
		else if (chooseURL.getValue().equals("coins.ha.com")){
			url = "coins.ha.com";
			urlSelected = true;
		}
		else urlSelected = false;
	}
}