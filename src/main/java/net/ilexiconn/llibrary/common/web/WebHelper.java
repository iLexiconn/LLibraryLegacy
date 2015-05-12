package net.ilexiconn.llibrary.common.web;

import com.google.common.collect.Lists;

import java.io.*;
import java.net.URL;
import java.util.List;

/**
 * Helper class to parse <a href="http://pastebin.com">Pastebin.com</a> pastes and read files from given URL
 *
 * @author Gegy1000, jglrxavpok
 */
public class WebHelper
{
    private static String pastebinURLPrefix = "http://pastebin.com/raw.php?i=";

    /**
     * Downloads the content of a text hosted on <a href="http://pastebin.com">Pastebin.com</a> line by line.
     *
     * @param pasteId The ID of the paste
     * @return The lines of the paste as a list
     * @throws IOException Thrown if there are problems while reading
     */
    public static List<String> readPastebinAsList(String pasteId) throws IOException
    {
        return downloadTextFileList(pastebinURLPrefix + pasteId);
    }

    /**
     * Downloads the content of a text hosted on <a href="http://pastebin.com">Pastebin.com</a>
     *
     * @param pasteId The ID of the paste
     * @return The content of the paste
     * @throws IOException Thrown if there are problems while reading
     */
    public static String readPastebin(String pasteId) throws IOException
    {
        return downloadTextFile(pastebinURLPrefix + pasteId);
    }

    /**
     * Downloads a text file from given URL line by line.
     *
     * @param urlString The URL to download from
     * @return The different lines of the file from first to last
     * @throws IOException Thrown if there are problems reading the file.
     */
    public static List<String> downloadTextFileList(String urlString) throws IOException
    {
        List<String> text = Lists.newArrayList();

        URL url = new URL(urlString);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String currentLine;

        while ((currentLine = reader.readLine()) != null)
            text.add(currentLine);
        reader.close();

        return text;
    }

    /**
     * Downloads a text file from given URL.
     *
     * @param urlString The URL to download from
     * @return The content of the file, as a String
     * @throws IOException Thrown if there are problems reading the file.
     */
    public static String downloadTextFile(String urlString) throws IOException
    {
        String text = "";

        URL url = new URL(urlString);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String currentLine;

        while ((currentLine = reader.readLine()) != null)
            text += currentLine + "\r\n";
        reader.close();

        return text;
    }

    /**
     * Downloads a file from given URL.
     *
     * @param rawURL The URL to download the file from
     * @return A byte array containing all the content of the file
     * @throws IOException Thrown in case there are problems reading the file
     */
    public static byte[] download(String rawURL) throws IOException
    {
        URL url = new URL(rawURL);
        try
        {
            InputStream in = url.openStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int i;
            while ((i = in.read(buffer)) != -1) baos.write(buffer, 0, i);
            baos.flush();
            return baos.toByteArray();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
