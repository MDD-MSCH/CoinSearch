package bots;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.BadLocationException;
import javax.swing.text.EditorKit;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;



public class CoinCatalogBot {
	private WebDriver headlessdriver;
	private WebElement element;

	public CoinCatalogBot(String URL) {
		headlessdriver = new HtmlUnitDriver(true);
		headlessdriver.get(URL);
		headlessdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	public void searchFor(String values) {
		element = headlessdriver.findElement(By.name("f_parameter"));
		element.sendKeys(values);
		submit();
	}

	private void submit() {
		((JavascriptExecutor) headlessdriver).executeScript("document.suche_parameter_formular.submit();");
	}

	public void backToSearchpage() {
		headlessdriver.findElement(By.cssSelector("td.navigationselement:nth-child(3) > a:nth-child(1)")).click();
	}

	public String getFirstResultPageSource() {
		headlessdriver
				.findElement(By.cssSelector(
						"html body table.strukturtabelle tbody tr td.inhaltszelle div.divrahmen center table.tabelle_typ1 tbody tr td.tabelle_typ1_inhalt a"))
				.click();
		return headlessdriver.getPageSource();
	}

	public String getFirstResultURL() {
		headlessdriver
				.findElement(By.cssSelector(
						"html body table.strukturtabelle tbody tr td.inhaltszelle div.divrahmen center table.tabelle_typ1 tbody tr td.tabelle_typ1_inhalt a"))
				.click();
		return headlessdriver.getCurrentUrl();
	}

	public String getSecoundResultPageSource() {
		headlessdriver
				.findElement(By.cssSelector(
						".tabelle_typ1 > tbody:nth-child(1) > tr:nth-child(3) > td:nth-child(2) > a:nth-child(1)"))
				.click();
		return headlessdriver.getPageSource();
	}

	public String substringBefore(String string, String delimiter) {
		int pos = string.indexOf(delimiter);
		return pos >= 0 ? string.substring(0, pos) : string;
	}

	public String substringAfter(String string, String delimiter) {
		int pos = string.indexOf(delimiter);
		return pos >= 0 ? string.substring(pos + delimiter.length()) : "";
	}

	public String substringBetween(String text, String start, String end) {
		return text.substring(text.indexOf(start) + 1, text.indexOf(end));
	}

	public String substringsBetween(String text, String regex) {
		Pattern pattern = Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS);
		Matcher matcher = pattern.matcher(text);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			sb.append(matcher.group());
			System.out.println(matcher.group());
		}
		return sb.toString();
	}

	public void readHTML(String website) {
		URL url;
		URLConnection con;
		BufferedReader br;
		HTMLEditorKit editorKit;
		HTMLDocument htmlDoc;
		try {
			url = new URL(website);
			con = url.openConnection();
			br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			editorKit = new HTMLEditorKit();
			htmlDoc = new HTMLDocument();
			htmlDoc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
			editorKit.read(br, htmlDoc, 0);
			HTMLDocument.Iterator iter = htmlDoc.getIterator(HTML.Tag.TABLE);
			while (iter.isValid()) {
				System.out.println(iter.getAttributes().getAttribute(HTML.Attribute.CONTENT));
				iter.next();
			}
		} catch (IOException | BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public String getTabellenText(String uriStr, boolean span) {
		final StringBuffer buf = new StringBuffer(1000);
		try {
			HTMLDocument doc = new HTMLDocument() {

				private static final long serialVersionUID = -4746501619335809073L;

				public HTMLEditorKit.ParserCallback getReader(int pos) {
					return new HTMLEditorKit.ParserCallback() {
						HTML.Tag tg = null;

						public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
							tg = t;
							if (t == HTML.Tag.TABLE)
								buf.append("");
						}

						public void handleEndTag(HTML.Tag t, int pos) {
							if (t == HTML.Tag.TR || t == HTML.Tag.TH)
								buf.append("\n");
							if (t == HTML.Tag.TD)
								buf.append("\t");
						}
						public void handleText(char[] data, int pos) {
							if (tg == HTML.Tag.TD) {
								buf.append(data);
							}
							if (tg == HTML.Tag.A) {
								buf.append(data);
							}
							if (span){
								if (tg == HTML.Tag.SPAN){
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
