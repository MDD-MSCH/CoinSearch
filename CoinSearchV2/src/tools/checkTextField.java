package tools;

import java.util.List;

public class checkTextField {
	private int pass;

	public boolean onlyNumbers(String text) {
		if (text.matches("[0-9]+")) {
			return true;
		}
		return false;
	}

	public boolean onlyLetters(String text) {
		if (text.matches("[A-Za-z]+")) {
			return true;
		}
		return false;
	}

	public int checkIfStringIsNotEmpty(List<String> eingabe) {
		pass = 0;
		eingabe.forEach(s -> {
			if (s.length() > 0)
				pass++;
		});
		return pass;
	}
}