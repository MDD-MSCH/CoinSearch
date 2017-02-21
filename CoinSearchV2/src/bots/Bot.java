package bots;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public abstract class Bot {
	private WebDriver headlessdriver;
	private WebElement element;
	
	public WebDriver getHeadlessdriver() {
		return headlessdriver;
	}
	public void setHeadlessdriver(HtmlUnitDriver headlessdriver) {
		this.headlessdriver = headlessdriver;
	}
	public WebElement getElement() {
		return element;
	}
	public void setElement(WebElement element) {
		this.element = element;
	}
	
	public abstract String getResultPageSourceByCSSselector(String cssSelector);
	
	public abstract String getResultURLbyCSSselector(String cssSelector);
	
	public abstract void backToSearchpageByCSSselector(String cssSelector);
	
	public String getSubstringBefore(String text, String delimiter) {
		int pos = text.indexOf(delimiter);
		return pos >= 0 ? text.substring(0, pos) : text;
	}

	public String getSubstringAfter(String text, String delimiter) {
		int pos = text.indexOf(delimiter);
		return pos >= 0 ? text.substring(pos + delimiter.length()) : "";
	}

	public String getSubstringBetween(String text, String start, String end) {
		return text.substring(text.indexOf(start) + 1, text.indexOf(end));
	}
}
