package controller.tab;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;

import bots.CoinCatalogBot;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import tools.SystemProps;

public class Tab1Controller implements Runnable, SystemProps {
	private CoinCatalogBot cobo;
	private static final String RESULT_ONE = "html body table.strukturtabelle tbody tr td.inhaltszelle div.divrahmen center table.tabelle_typ1 tbody tr td.tabelle_typ1_inhalt a";
	private static final String RESULT_TWO = ".tabelle_typ1 > tbody:nth-child(1) > tr:nth-child(3) > td:nth-child(2) > a:nth-child(1)";
	private static final String RESULT_THREE = ".tabelle_typ1 > tbody:nth-child(1) > tr:nth-child(5) > td:nth-child(2) > a:nth-child(1)";
	private static final String START1 = "Allgemeine Daten";
	private static final String END1 = "Katalognummern";
	private static final String START2 = "Details";
	private static final String END2 = "weitere Hinweise finden Sie in der";

	private String keywords;
	public String text, c;

	@FXML
	public TextArea texts;

	@FXML
	public Button load;

	@FXML
	public void sendText() {
		System.out.println(text);
		try (BufferedReader bfrd = new BufferedReader(
				new InputStreamReader(new FileInputStream(FULL_PATH_TO_RESULTS_NAME), "UTF-8"))) {
			while ((c = bfrd.readLine()) != null) {
				texts.appendText(c + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void initialize() {

	}

	@Override
	public void run() {
		try {
			lookaround();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void lookaround() throws MalformedURLException, IOException {
		keywords = getValues();
		StringBuffer collectedResults = new StringBuffer();
		try {
			if (SearchTab.getUrl().equals("muenzkatalog-online.de")) {
				cobo = new CoinCatalogBot("http://www.muenzkatalog-online.de/index.php?cstart=0&cstop=400");
				cobo.searchFor("1 Deutsche Mark"); // keywords
				if (cobo.checkIfCoinExsist()) {
					collectedResults.append(getCollectedResult(RESULT_ONE).concat("\n"));
					collectedResults.append(getCollectedResult(RESULT_TWO).concat("\n"));
					collectedResults.append(getCollectedResult(RESULT_THREE).concat("\n"));
					writeCollectedResults(collectedResults.toString());
				}
			}
			if (SearchTab.getUrl().equals("baldwin.co.uk")) {
				// Todo Bot for "http://www.baldwin.co.uk/coins.html?limit=all";
			}
			if (SearchTab.getUrl().equals("coins.ha.com")) {
				// Todo Bot for "http://www.coins.ha.com";
			}

		} catch (org.openqa.selenium.remote.UnreachableBrowserException e) {
			System.out.println("Error communicating with the remote browser. It may have died.");
		}
	}

	private String getValues() {
		StringBuilder values = new StringBuilder();

		String value1 = SearchTab.getCheckWert();
		if (value1 != null) {
			values.append(value1 + ";");
		}
		String value2 = SearchTab.getCheckWaehrung();
		if (value2 != null) {
			values.append(value2 + ";");
		}
		String value3 = SearchTab.getCheckJahr();
		if (value3 != null) {
			values.append(value3 + ";");
		}
		String value4 = SearchTab.getCheckInschriftKopf();
		if (value4 != null) {
			values.append(value4 + ";");
		}

		String value5 = SearchTab.getCheckInschriftZahl();
		if (value5 != null) {
			values.append(value5 + ";");
		}
		String value6 = SearchTab.getCheckZustand();
		if (value6 != null) {
			values.append(value6 + ";");
		}
		String value7 = SearchTab.getCheckPraegeort();
		if (value7 != null) {
			values.append(value7 + ";");
		}
		return values.toString();
	}

	private String getCollectedResult(String cssSelector) {
		StringBuffer result = new StringBuffer();
		String pageSource = cobo.getResultPageSourceByCSSselector(cssSelector);
		String coinInfo = cobo.getSubstringBetween(pageSource, START1, END1);
		result.append("Result: \n".concat(cobo.getTableText(coinInfo, false)).concat("\n"));

		String priceInfo = cobo.getSubstringBetween(pageSource, START2, END2);
		result.append(cobo.getTableText(priceInfo, true));
		return result.toString();
	}

	private void writeCollectedResults(String results) {
		try (BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(FULL_PATH_TO_RESULTS_NAME), "UTF-8"))) {
			writer.write(results);
			writer.flush();
			System.out.println("Done"); // to do some information for the user
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
