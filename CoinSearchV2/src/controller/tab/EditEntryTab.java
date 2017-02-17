package controller.tab;

import java.sql.SQLException;
import javax.swing.JOptionPane;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tools.ConnectionHelper;
import tools.DBconnection;

public class EditEntryTab extends ConnectionHelper{
	

	@FXML
	private Label id, wertLabel, waehrungLabel, jahrLabel, inschriftKopfLabel, inschriftZahlLabel, zustandLabel,
			praegeortLabel;
	@FXML
	private TextField wert, waehrung, jahr, inschriftKopf, inschriftZahl, zustand, praegeort;

	@FXML
	public void initialize() {
		getConnection();
		try {
			if (resultset.next()) {
				setData();
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
			zustand.setText(resultset.getString(7));
			praegeort.setText(resultset.getString(8));
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Problem by reading: \n" + e.toString());
			e.printStackTrace();
		}
	}

	@FXML
	private void save() {
		String query = "UPDATE muenzen SET wert = '" + wert.getText() + "', waehrung = '" + waehrung.getText() 
				+ "', jahr = '"+ jahr.getText() + "', inschriftKopf = '" + inschriftKopf.getText() 
				+ "', inschriftZahl = '"+ inschriftZahl.getText() + "', zustand = '" + zustand.getText() 
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
			JOptionPane.showMessageDialog(null, "Problem by saving: \n" + e.toString());
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
			JOptionPane.showMessageDialog(null, "Problem by deleting: \n" + e.toString());
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