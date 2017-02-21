package bots;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import javax.swing.text.BadLocationException;
import javax.swing.text.EditorKit;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.openqa.selenium.By;
import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

// First result cssSelector //"html body table.strukturtabelle tbody tr td.inhaltszelle div.divrahmen center table.tabelle_typ1 tbody tr td.tabelle_typ1_inhalt a"
// Secound result cssSelector ".tabelle_typ1 > tbody:nth-child(1) > tr:nth-child(3) > td:nth-child(2) > a:nth-child(1)"
// Back to searchpage CSS selector: "td.navigationselement:nth-child(3) > a:nth-child(1)"
public class CoinCatalogBot extends Bot {

	public CoinCatalogBot(String URL) {
		setHeadlessdriver(new HtmlUnitDriver(true));
		getHeadlessdriver().get(URL);
		getHeadlessdriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	public void searchFor(String values) {
		setElement(getHeadlessdriver().findElement(By.name("f_parameter")));
		getElement().sendKeys(values);
		submitByJavascript("document.suche_parameter_formular.submit();");
	}

	private void submitByJavascript(String javascript) {
		((JavascriptExecutor) getHeadlessdriver()).executeScript(javascript);
	}
	
	@Override
	public boolean checkIfCoinExsist(){
		try{
			getResultPageSourceByCSSselector(".error");
		}catch(org.openqa.selenium.NoSuchElementException e){
			System.out.println("Coin found");
			return true;
		}
		return false;
	}
	
	@Override
	public String getResultPageSourceByCSSselector(String cssSelector) {
		getHeadlessdriver().findElement(By.cssSelector(cssSelector)).click();
		return getHeadlessdriver().getPageSource();
	}

	@Override
	public String getResultURLbyCSSselector(String cssSelector) {
		getHeadlessdriver().findElement(By.cssSelector(cssSelector)).click();
		return getHeadlessdriver().getCurrentUrl();
	}

	@Override
	public void backToSearchpageByCSSselector(String cssSelector) {
		getHeadlessdriver().findElement(By.cssSelector(cssSelector)).click();
	}

	public String getTableText(String uriStr, boolean span) {
		final StringBuffer buf = new StringBuffer(1000);
		try {
			HTMLDocument doc = new HTMLDocument() {

				private static final long serialVersionUID = -4746501619335809073L;

				public HTMLEditorKit.ParserCallback getReader(int pos) {
					return new HTMLEditorKit.ParserCallback() {
						HTML.Tag tag = null;

						public void handleStartTag(HTML.Tag tag, MutableAttributeSet a, int pos) {
							this.tag = tag;
							if (tag == HTML.Tag.TABLE){
								buf.append("");
							}
						}

						public void handleEndTag(HTML.Tag tag, int pos) {
							if (tag == HTML.Tag.TR || tag == HTML.Tag.TH){
								buf.append("\n");
							}
							if (tag == HTML.Tag.TD){
								buf.append("\t");
							}
						}

						public void handleText(char[] data, int pos) {
							if (tag == HTML.Tag.TD) {
								buf.append(data);
							}
							if (tag == HTML.Tag.A) {
								buf.append(data);
							}
							if (span) {
								if (tag == HTML.Tag.SPAN) {
									buf.append(data);
								}
							}
						}
					};
				}
			};
			doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
			Reader rd = new StringReader(uriStr);

			EditorKit kit = new HTMLEditorKit();
			kit.read(rd, doc, 0);

		} catch (MalformedURLException e) {
			System.out.println(e);
		} catch (BadLocationException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
		return buf.toString().trim();
	}
}