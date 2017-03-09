package controller.tab;

import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tools.ConnectionHelper;
import tools.DBconnection;
import tools.LetterTextField;
import tools.NumberTextField;

public class EditEntryTab extends ConnectionHelper{
	

	@FXML
	private Label id, wertLabel, waehrungLabel, jahrLabel, inschriftKopfLabel, inschriftZahlLabel, zustandLabel,
			praegeortLabel;
	@FXML
	private TextField wert,  inschriftKopf, inschriftZahl, praegeort;
	
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
		try {
			if (resultset.next()) {
				setData();
			}
		} catch (SQLException e) {
			errorAlert.setContentText("Problem by initialising: \n" + e.toString());
			errorAlert.show();
		}
	}

	private void setData() {
		try {
			id.setText(Integer.toString(resultset.getInt(1)));
			wert.setText(resultset.getString(2));
			waehrung.setText(resultset.getString(3));
			jahr.setText(Integer.toString(resultset.getInt(4)));
			inschriftKopf.setText(resultset.getString(5));
			inschriftZahl.setText(resultset.getString(6));
			zustand.setValue(resultset.getString(7));
			praegeort.setText(resultset.getString(8));
		} catch (SQLException e) {
			errorAlert.setContentText("Problem by reading: \n" + e.toString());
			errorAlert.show();
		}
	}

	@FXML
	private void save() {
		String query = "UPDATE muenzen SET wert = '" + wert.getText() + "', waehrung = '" + waehrung.getText() 
				+ "', jahr = '"+ jahr.getText() + "', inschriftKopf = '" + inschriftKopf.getText() 
				+ "', inschriftZahl = '"+ inschriftZahl.getText() + "', zustand = '" + zustand.getValue() 
				+ "', praegeort = '"+ praegeort.getText() + "' WHERE id = '" + id.getText() + "'";
		try {
			int position;
			position = resultset.getRow();
			prepStatement = connection.prepareStatement(query);
			prepStatement.executeQuery();
			DBconnection.INSTANCE.refreshConnection();
			getConnection();
			resultset.absolute(position);
			if (resultset.isAfterLast())
				resultset.last();
			setData();
		} catch (SQLException e) {
			errorAlert.setContentText("Problem by saving: \n" + e.toString());
			errorAlert.show();
		}
	}

	@FXML
	private void delete() {
		String query = "DELETE FROM muenzen WHERE id = '" + id.getText() + "'";
		try {
			int position;
			position = resultset.getRow();
			prepStatement = connection.prepareStatement(query);
			prepStatement.executeQuery();
			DBconnection.INSTANCE.refreshConnection();
			getConnection();
			resultset.absolute(position);
			if (resultset.isAfterLast()){
				resultset.first();
			}
			if(resultset.isBeforeFirst()){
				resultset.last();
			}
			setData();
		} catch (SQLException e) {
			errorAlert.setContentText("Problem by delating: \n" + e.toString());
			errorAlert.show();
		}
	}
	
	@FXML
	private void ganzVor() {
		if(first()){
			setData();
		}	
	}

	@FXML
	private void ganzZurueck() {
		if(last()){
			setData();
		}		
	}

	@FXML
	private void einenVor() {
		if(oneForward()){
			setData();
		}
	}

	@FXML
	private void einenZurueck() {
		if(oneBackward()){
			setData();
		}
	}
}