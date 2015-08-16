package net.ilexiconn.llibrary.common.web;

import com.google.common.collect.Lists;
import net.ilexiconn.llibrary.LLibrary;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Helper class to parse <a href="http://pastebin.com">Pastebin.com</a> pastes and read files from given URL
 *
 * @author Gegy1000
 * @author jglrxavpok
 * @author FiskFille
 * @author iLexiconn
 * @since 0.1.0
 */
public class WebHelper
{
    private static String pastebinURLPrefix = "http://pastebin.com/raw.php?i=";

    /**
     * Downloads the content of a text hosted on <a href="http://pastebin.com">Pastebin</a> line by line.
     *
     * @param pasteIds An array of paste IDs, data from the first working ID will be used
     * @return The lines of the paste as a list
     */
    public static List<String> readPastebinAsList(String... pasteIds)
    {
        for (int i = 0; i < pasteIds.length; i++)
            pasteIds[i] = pastebinURLPrefix + pasteIds[i];
        return downloadTextFileList(pasteIds);
    }

    /**
     * Downloads the content of a text hosted on <a href="http://pastebin.com">Pastebin</a>
     *
     * @param pasteIds An array of paste IDs, data from the first working ID will be used
     * @return The content of the paste
     */
    public static String readPastebin(String... pasteIds)
    {
        for (int i = 0; i < pasteIds.length; i++)
            pasteIds[i] = pastebinURLPrefix + pasteIds[i];
        return downloadTextFile(pasteIds);
    }

    /**
     * Downloads a text file from given URL line by line.
     *
     * @param urlStrings An array of links, receive data from the first working URL in the array
     * @return The different lines of the file from first to last
     */
    public static List<String> downloadTextFileList(String... urlStrings)
    {
        for (String urlString : urlStrings)
        {
            try
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
            catch (IOException e)
            {
                LLibrary.logger.error("Failed receiving data from url '" + urlString + "'. (" + e.getLocalizedMessage() + ")");
            }
        }
        LLibrary.logger.error("None of the given urls worked! " + Arrays.toString(urlStrings));
        return null;
    }

    /**
     * Downloads a text file from given URL.
     *
     * @param urlStrings An array of links, receive data from the first working URL in the array
     * @return The content of the file, as a String
     */
    public static String downloadTextFile(String... urlStrings)
    {
        for (String urlString : urlStrings)
        {
            try
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
            catch (IOException e)
            {
                LLibrary.logger.error("Failed receiving data from url '" + urlString + "'. (" + e.getLocalizedMessage() + ")");
            }
        }
        LLibrary.logger.error("None of the given urls worked! " + Arrays.toString(urlStrings));
        return null;
    }

    /**
     * Downloads a file from given URL.
     *
     * @param urlStrings An array of links, receive data from the first working URL in the array
     * @return A byte array containing all the content of the file
     */
    public static byte[] download(String... urlStrings)
    {
        for (String urlString : urlStrings)
        {
            try
            {
                URL url = new URL(urlString);
                InputStream in = url.openStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int k;
                while ((k = in.read(buffer)) != -1)
                    baos.write(buffer, 0, k);
                baos.flush();
                return baos.toByteArray();
            }
            catch (IOException e)
            {
                LLibrary.logger.error("Failed receiving data from url '" + urlString + "'. (" + e.getLocalizedMessage() + ")");
            }
        }
        LLibrary.logger.error("None of the given urls worked! " + Arrays.toString(urlStrings));
        return null;
    }

    /**
     * Downloads an image from given URL.
     *
     * @param urlStrings An array of links, receive data from the first working URL in the array
     * @return A BufferedImage object downloaded from the given URL
     */
    public static BufferedImage downloadImage(String... urlStrings)
    {
        for (String urlString : urlStrings)
        {
            try
            {
                URL url = new URL(urlString);
                InputStream in = new BufferedInputStream(url.openStream());
                return ImageIO.read(in);
            }
            catch (IOException e)
            {
                LLibrary.logger.error("Failed receiving data from url '" + urlString + "'. (" + e.getLocalizedMessage() + ")");
            }
        }
        LLibrary.logger.error("None of the given urls worked! " + Arrays.toString(urlStrings));
        return null;
    }
}
