package tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebsiteReader {

	     public String getStrFromUrl(String surl){
	    	 final String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; de; rv:1.8.1.12) Gecko/20080201 Firefox/2.0.0.12";
	    	 StringBuilder strbuild=null;
	    	 String str1=null;
	    	 String str=null;
	    	 try {
	    		 URL url = new URL(surl);
	    		 URLConnection conn = url.openConnection();
	    		 conn.addRequestProperty("User-Agent", userAgent);
           		 BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	    		 strbuild = new StringBuilder();
	    		 while ((str1 = in.readLine()) != null) {
	    			Pattern begriff =Pattern.compile(""); //getKeywords
	    			Matcher m = begriff.matcher(str1);
	    					while(m.find()){
	    					//	System.out.println(m.group());
	    						strbuild.append(str1+"\r\n");
	    					}
	    		}
	    		 in.close();
	    		 str = strbuild.toString();
	    		 return str;
	    	 } catch (MalformedURLException e)
	    	 {   System.out.println( e.getMessage());
	    	 }
	    	 catch (IOException e)
	    	 {   System.out.println( e.getMessage());
	    	 }
	    	 return str;
	     }
}