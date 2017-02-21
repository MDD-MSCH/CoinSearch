package controller.tab;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import bots.CoinCatalogBot;
import controller.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import tools.WebsiteReader;


public class Tab1Controller implements Runnable{
	private static final String start1 = "Allgemeine Daten";
	private static final String end1 = "Katalognummern";
	private static final String start2 = "Details";
	private static final String end2 = "weitere Hinweise finden Sie in der";
	//public MainController main;
	private WebDriver foxdriver;
	private WebDriverWait wait;
	private String keywords;
	public String text, c;
	
			
	@FXML
	public TextArea texts;	
	
	@FXML
	public Button load;
	
	@FXML
	public void sendText(){
		System.out.println(text);
		try (BufferedReader bfrd = new BufferedReader(new InputStreamReader(new FileInputStream("temp1.txt"), "UTF-8"))){
			while ((c = bfrd.readLine()) != null) {
				texts.appendText(c+"\n");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}	
	}
	@FXML
	public void initialize() {
		
	}

	/*public void init(MainController mainController) {
		main = mainController;
		
	}	*/
			
	public String getKeywords() {
		return keywords;
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
	
	private void lookaround() throws MalformedURLException, IOException{
		keywords= getValues();
		foxdriver = new FirefoxDriver();	
		wait = new WebDriverWait(foxdriver, 10);
		String url = "";
		List<String> list1 = new ArrayList<String>();

		try{
			if (SearchTab.getUrl().equals("muenzkatalog-online.de")){
				CoinCatalogBot cobo = new CoinCatalogBot("http://www.muenzkatalog-online.de/index.php?cstart=0&cstop=400");
				cobo.searchFor("1 Deutsche Mark");
				String text = cobo.getFirstResultPageSource();
				String d = cobo.substringBetween(text, start1, end1);
				String tabelle = cobo.getTabellenText(d, false);
				System.out.println(tabelle);
				
				String e = cobo.substringBetween(text, start2, end2);
				String tabelle2 = cobo.getTabellenText(e, true);
				System.out.println(tabelle2);
			}
			if (SearchTab.getUrl().equals("baldwin.co.uk")){
			foxdriver.get("http://www.baldwin.co.uk/coins.html?limit=all");
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input.dropdown.dd-all")));
			url = "http://www.baldwin.co.uk/coins.html?limit=all";
			
			}else if(SearchTab.getUrl().equals("coins.ha.com")){ 
			foxdriver.get("http://www.coins.ha.com");
			url = "http://www.coins.ha.com";
			}
//			list1 = new LinkGetter().getLinks(url);
//			System.out.println( list1 );   
			reading(url);
			//texts.setText("test");
		} catch (org.openqa.selenium.remote.UnreachableBrowserException e){
				System.out.println("Error communicating with the remote browser. It may have died.");
			}
		}
	private String getValues(){ //Lamda??
		StringBuilder values = new StringBuilder();
		
		String value1 = SearchTab.getCheckWert();
		if (value1 != null){
			values.append(value1+";");
		}
		String value2 = SearchTab.getCheckWaehrung();
		if (value2 != null){
			values.append(value2+";");
		}
		String value3 = SearchTab.getCheckJahr();
		if (value3 != null){
			values.append(value3+";");
		}
		String value4 = SearchTab.getCheckInschriftKopf();
		if (value4 != null){
			values.append(value4+";");
		}
				
		String value5 = SearchTab.getCheckInschriftZahl();
		if (value5 != null){
			values.append(value5+";");
		}
		String value6 = SearchTab.getCheckZustand();
		if (value6 != null){
			values.append(value6+";");
		}
		String value7 = SearchTab.getCheckPraegeort();
		if (value7 != null){
			values.append(value7+";");
		}
		return values.toString();	
	}
	
	private void reading(String url){
//		List<String> list1 = new ArrayList<String>();
	 File file = new File("temp1.txt");
     FileOutputStream ausgabe = null;
        try {
            ausgabe = new FileOutputStream(file);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        DataOutputStream raus = new DataOutputStream(ausgabe);
        text=new WebsiteReader().getStrFromUrl(url);
//        list1 = new LinkGetter().getLinks(test);
        try {
            raus.writeBytes(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
