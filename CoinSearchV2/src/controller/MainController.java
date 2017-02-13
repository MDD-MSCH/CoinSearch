package controller;


import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import controller.tab.*;

public class MainController {

	@FXML public Tab1Controller tab1Controller;
	
	@FXML public void initialize() {
		System.out.println("Start");
//		tab1Controller.init(this);
		//searchTab.init(this);
	}
	
	public String setTab1Text(String text){
		System.out.println(text+"\n\t");
		return text;
	}
}