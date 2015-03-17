package net.ilexiconn.llibrary.web;

import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

/**
 * Helper class to parse pastebin pastes.
 *
 * @author Gegy1000
 */
public class WebHelper 
{
	private static String pastebinURLPrefix = "http://pastebin.com/raw.php?i=";
	
	public static List<String> readPastebinAsList(String pasteId) throws IOException
	{
		return downloadFileList(pastebinURLPrefix + pasteId);
	}
	
	public static String readPastebin(String pasteId) throws IOException
	{
		return downloadFile(pastebinURLPrefix + pasteId);
	}
	
	public static List<String> downloadFileList(String urlString) throws IOException
	{
		List<String> text = Lists.newArrayList();

        URL url = new URL(urlString);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
		String currentLine;
		
		while ((currentLine = reader.readLine()) != null) text.add(currentLine);
		reader.close();
		
		return text;
	}
	
	public static String downloadFile(String urlString) throws IOException
	{
		String text = "";
		
		URL url = new URL(urlString);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
		String currentLine;
		
		while ((currentLine = reader.readLine()) != null) text += currentLine + "\r\n";
		reader.close();
		
		return text;
	}
}
