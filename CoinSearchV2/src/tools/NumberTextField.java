package tools;

import javafx.scene.control.TextField;

public class NumberTextField extends TextField {
    
    @Override public void replaceText(int start, int end, String text) {
           if (text.matches("\\d*") || text == "") {
               super.replaceText(start, end, text);
           }
       }
     
       @Override public void replaceSelection(String text) {
           if (text.matches("\\d*") || text == "") {
               super.replaceSelection(text);
           }
       }
}