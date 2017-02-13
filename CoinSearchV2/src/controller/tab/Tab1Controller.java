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

import controller.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import tools.WebsiteReader;


public class Tab1Controller implements Runnable{
	
	//public MainController main;
	private WebDriver foxdriver;
	private WebDriverWait wait;
	private static String keywords;
	public String text, readString;
	//private BufferedReader bfrd;
		
	@FXML
	public TextArea texts;	
	
	@FXML
	public Button load;
	
	@FXML
	public void sendText(){
		System.out.println(text);
		try (Reader bfrd = new BufferedReader(new InputStreamReader(new FileInputStream("temp1.txt"), "UTF-8"))){
			while ((readString= bfrd.read()) != null) {
				texts.appendText(readString+"\n");
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
	
		
	public static String getKeywords() {
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
		getValues();
		foxdriver = new FirefoxDriver();	
		wait = new WebDriverWait(foxdriver, 10);
		String url = "";
		List<String> list1 = new ArrayList<String>();

		try{
			if (SearchTab.getUrl().equals("baldwin.co.uk")){
			foxdriver.get("http://www.baldwin.co.uk/coins.html?limit=all");
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input.dropdown.dd-all")));
			url = "http://www.baldwin.co.uk/coins.html?limit=all";
			}else if(SearchTab.getUrl().equals("coins.ha.com")){ //http://www.impressionlink.com/support/
			foxdriver.get("http://www.impressionlink.com/support/");	//http://www.coins.ha.com
			url = "http://www.impressionlink.com/support/"; //http://www.coins.ha.com
			}
//			list1 = new LinkGetter().getLinks(url);
//			System.out.println( list1 );   
			reading(url);
			//texts.setText("test");
		} catch (org.openqa.selenium.remote.UnreachableBrowserException e){
				System.out.println("Error communicating with the remote browser. It may have died.");
			}
		}
	private void getValues(){ //Lamda??
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
		keywords=values.toString();	
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
