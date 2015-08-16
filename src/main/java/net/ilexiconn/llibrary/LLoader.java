package net.ilexiconn.llibrary;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.relauncher.FMLInjectionData;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.IFMLCallHook;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import sun.misc.URLClassPath;
import sun.net.util.URLUtil;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * For autodownloading stuff.
 * This is really unoriginal, mostly ripped off ChickenBones who ripped it off from FML, credits to cpw
 *
 * @author Ry_dog101
 */
public class LLoader implements IFMLLoadingPlugin, IFMLCallHook
{
    private static final String owner = "LLoader";
    private static ByteBuffer downloadBuffer = ByteBuffer.allocateDirect(1 << 23);
    private static LLoadInst inst;

    public static void load()
    {
        if (inst == null)
        {
            inst = new LLoadInst();
            LLoadInst.scanForJson();
            for (File file : LLoadInst.llibraryData.listFiles())
                LLoadInst.addClasspath(file.getName());
        }
    }

    public String[] getASMTransformerClass()
    {
        return null;
    }

    public String getModContainerClass()
    {
        return null;
    }

    public String getSetupClass()
    {
        return this.getClass().getName();
    }

    public void injectData(Map<String, Object> data)
    {

    }

    public Void call()
    {
        load();
        return null;
    }

    public String getAccessTransformerClass()
    {
        return null;
    }

    public interface IDownloadDisplay
    {
        void resetProgress(int sizeGuess);

        void setPokeThread(Thread currentThread);

        void updateProgress(int fullLength);

        boolean shouldStopIt();

        void getModName(String mod);

        void getFileName(String mod);

        Object makeDialog();

        void showErrorDialog(String name, String url);
    }

    public static class Downloader extends JOptionPane implements IDownloadDisplay
    {
        boolean stopIt;
        Thread pokeThread;
        private JDialog container;
        private JTextField modName;
        private JTextField fileName;
        private JProgressBar progress;

        private Box makeProgressPanel()
        {
            Box box = Box.createVerticalBox();
            box.add(Box.createRigidArea(new Dimension(0, 10)));
            modName = new JTextField("modName");
            modName.setEditable(false);
            box.add(modName);
            box.add(Box.createRigidArea(new Dimension(0, 10)));
            fileName = new JTextField("file");
            fileName.setEditable(false);
            box.add(fileName);
            box.add(Box.createRigidArea(new Dimension(0, 10)));
            progress = new JProgressBar(0, 100);
            progress.setStringPainted(true);
            box.add(progress);
            box.add(Box.createRigidArea(new Dimension(0, 30)));
            return box;
        }

        public JDialog makeDialog()
        {
            try
            {
                if (container != null)
                    return container;

                setMessage(makeProgressPanel());
                setOptions(new Object[]{"Stop"});
                addPropertyChangeListener(new PropertyChangeListener()
                {
                    public void propertyChange(PropertyChangeEvent evt)
                    {
                        if (evt.getSource() == Downloader.this && evt.getPropertyName().equals(VALUE_PROPERTY))
                        {
                            requestClose("This will stop minecraft from launching\nAre you sure you want to do this?");
                        }
                    }
                });
                container = new JDialog(null, "LLoader", ModalityType.MODELESS);
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                container.setResizable(false);
                container.setLocationRelativeTo(null);
                container.add(this);
                this.updateUI();
                container.pack();
                container.setMinimumSize(container.getPreferredSize());
                container.setVisible(true);
                container.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                container.addWindowListener(new WindowAdapter()
                {
                    public void windowClosing(WindowEvent e)
                    {
                        requestClose("Closing this window will stop minecraft from launching\nAre you sure you wish to do this?");
                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return container;
        }

        protected void requestClose(String message)
        {
            int shouldClose = JOptionPane.showConfirmDialog(container, message, "Are you sure you want to stop?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (shouldClose == JOptionPane.YES_OPTION)
                container.dispose();

            stopIt = true;
            if (pokeThread != null)
                pokeThread.interrupt();
        }

        public void resetProgress(int sizeGuess)
        {
            if (progress != null)
                progress.getModel().setRangeProperties(0, 0, 0, sizeGuess, false);
        }

        public void updateProgress(int fullLength)
        {
            if (progress != null)
                progress.getModel().setValue(fullLength);
        }

        public void getModName(String mod)
        {
            if (modName != null)
                modName.setText(mod);
        }

        public void getFileName(String file)
        {
            if (fileName != null)
                fileName.setText(file);
        }

        public void setPokeThread(Thread currentThread)
        {
            this.pokeThread = currentThread;
        }

        public boolean shouldStopIt()
        {
            return stopIt;
        }

        public void showErrorDialog(String name, String url)
        {
            JEditorPane ep = new JEditorPane("text/html",
                    "<html>" +
                            owner + " was unable to download required library " + name +
                            "<br>Check your internet connection and try restarting or download it manually from" +
                            "<br><a href=\"" + url + "\">" + url + "</a> and put it in your mods folder" +
                            "</html>");

            ep.setEditable(false);
            ep.setOpaque(false);
            ep.addHyperlinkListener(new HyperlinkListener()
            {
                public void hyperlinkUpdate(HyperlinkEvent event)
                {
                    try
                    {
                        if (event.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
                            Desktop.getDesktop().browse(event.getURL().toURI());
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            JOptionPane.showMessageDialog(null, ep, "A download error has occured", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static class LLoadInst
    {
        private static File modsDir;
        private static File v_modsDir;
        private static File llibraryData;
        private static IDownloadDisplay downloadMonitor;
        private static JDialog popupWindow;

        public LLoadInst()
        {
            String mcVer = (String) FMLInjectionData.data()[4];
            File mcDir = (File) FMLInjectionData.data()[6];

            modsDir = new File(mcDir, "mods");
            v_modsDir = new File(mcDir, "mods/" + mcVer);
            llibraryData = new File(mcDir, "llibrary/data/" + mcVer);
            if (!v_modsDir.exists())
                v_modsDir.mkdirs();
            if (!llibraryData.exists())
                llibraryData.mkdirs();
        }

        private static List<File> files()
        {
            List<File> list = new LinkedList<File>();
            list.addAll(Arrays.asList(modsDir.listFiles()));
            list.addAll(Arrays.asList(v_modsDir.listFiles()));
            list.addAll(Arrays.asList(llibraryData.listFiles()));
            return list;
        }

        /**
         * Scan list of files for jar/zip files with a downloads.json
         * But also get Mod Name from Mods mcmod.info file
         */
        private static void scanForJson()
        {
            try
            {
                for (File file : files())
                {
                    if (file.getName().endsWith(".jar") || file.getName().endsWith(".zip"))
                    {
                        ZipFile zip = new ZipFile(file);
                        ZipEntry e = zip.getEntry("llibrary/downloads.json");
                        ZipEntry info = zip.getEntry("mcmod.info");
                        String modName = "Unknown";
                        if (info != null)
                            modName = new JsonParser().parse(new InputStreamReader(zip.getInputStream(info))).getAsJsonArray().get(0).getAsJsonObject().get("name").getAsString();
                        if (e != null)
                            loadJson(new InputStreamReader(zip.getInputStream(e)), modName, zip.getName());
                        zip.close();
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        /**
         * Load the downloads.json file and get name, url and target for download files
         */
        private static void loadJson(InputStreamReader input, String modName, String modFile)
        {
            try
            {
                JsonElement root = new JsonParser().parse(input);
                String url = root.getAsJsonArray().get(0).getAsString();
                URL test = new URL(url);
                BufferedReader in = new BufferedReader(new InputStreamReader(test.openStream()));
                JsonArray obj = new JsonParser().parse(in).getAsJsonArray();
                for (int i = 0; i < obj.size(); i++)
                {
                    String downloadName = obj.get(i).getAsJsonObject().get("name").getAsString();
                    String downloadURL = obj.get(i).getAsJsonObject().get("url").getAsString();
                    String downloadTarget = obj.get(i).getAsJsonObject().get("target").getAsString();
                    download(downloadURL, downloadTarget, downloadName, modName, new File(modFile));
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        /**
         * Checks if file about to be downloaded exists or not. But also checks if older version exists (File with same Base Name)
         *
         * @param target download target file name
         * @param name   download file Base Name
         * @return whether file exists or not
         */
        private static boolean checkExisting(String target, String name)
        {
            for (File file : files())
            {
                if (file.getName().contains(name) && !file.getName().equals(target))
                {
                    delete(file);
                    return true;
                }
                if (file.getName().equals(target))
                {
                    return false;
                }
            }
            return true;
        }

        /**
         * Deletes files in necessary
         *
         * @param target file to delete
         */
        private static void delete(File target)
        {
            if (target.delete())
                return;

            if (!target.delete())
            {
                target.deleteOnExit();
                String msg = owner + " was unable to delete file " + target.getPath() + " the game will now try to delete it on exit. If this dialog appears again, delete it manually.";
                System.err.println(msg);
                if (!GraphicsEnvironment.isHeadless())
                    JOptionPane.showMessageDialog(null, msg, "An update error has occured", JOptionPane.ERROR_MESSAGE);

                System.exit(1);
            }

        }

        /**
         * Used to get everything ready for downloading files
         *
         * @param url     URL to download file from
         * @param target  Where to download file to
         * @param name    Base name of download file
         * @param modName Name of mod requiring the file
         */
        private static void download(String url, String target, String name, String modName, File modFile)
        {
            File libFile = new File(llibraryData, target);
            try
            {
                if (!checkExisting(target, name))
                    return;

                if (FMLLaunchHandler.side().isClient())
                    downloadMonitor = new Downloader();

                popupWindow = (JDialog) downloadMonitor.makeDialog();
                downloadMonitor.getFileName(target);
                downloadMonitor.getModName(modName);
                URL libDownload = new URL(url);
                System.out.format("Downloading file %s\n", libDownload.toString());
                URLConnection connection = libDownload.openConnection();
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.setRequestProperty("User-Agent", "" + owner + " Downloader");
                int sizeGuess = connection.getContentLength();
                download(connection.getInputStream(), sizeGuess, libFile);
                System.out.println("Download complete");
            }
            catch (Exception e)
            {
                libFile.delete();
                if (downloadMonitor.shouldStopIt())
                {
                    System.err.println("You have stopped the downloading operation before it could complete");
                    System.err.println("So we're going to remove " + modName + " from the Classpath to stop it from breaking things");
                    removeClasspath(modFile);
                }
                downloadMonitor.showErrorDialog(libFile.getName(), url);
                throw new RuntimeException("A download error occured", e);
            }
        }

        /**
         * Used to do the actual downloading of files
         *
         * @param is        InputStream of file to be downloaded
         * @param sizeGuess Estimate of file size
         * @param target    Where the downloaded file should be saved
         * @throws Exception
         */
        private static void download(InputStream is, int sizeGuess, File target) throws Exception
        {
            try
            {
                if (sizeGuess > downloadBuffer.capacity())
                    throw new Exception(String.format("The file %s is too large to be downloaded by " + owner + " - the download is invalid", target.getName()));

                downloadBuffer.clear();


                int bytesRead, fullLength = 0;

                downloadMonitor.resetProgress(sizeGuess);
                downloadMonitor.setPokeThread(Thread.currentThread());
                byte[] smallBuffer = new byte[1024];
                while ((bytesRead = is.read(smallBuffer)) >= 0)
                {
                    downloadBuffer.put(smallBuffer, 0, bytesRead);
                    fullLength += bytesRead;
                    if (downloadMonitor.shouldStopIt())
                    {
                        break;
                    }
                    downloadMonitor.updateProgress(fullLength);
                }
                is.close();
                downloadMonitor.setPokeThread(null);
                downloadBuffer.limit(fullLength);
                downloadBuffer.position(0);
            }
            catch (InterruptedIOException e)
            {
                Thread.interrupted();
                throw new Exception("Stop");
            }
            catch (IOException e)
            {
                throw e;
            }

            try
            {
                if (!target.exists())
                    target.createNewFile();


                downloadBuffer.position(0);
                FileOutputStream fos = new FileOutputStream(target);
                fos.getChannel().write(downloadBuffer);
                fos.close();
            }
            catch (Exception e)
            {
                throw e;
            }
            finally
            {
                if (popupWindow != null)
                {
                    popupWindow.setVisible(false);
                    popupWindow.dispose();
                }
            }
        }

        /**
         * Used to add downloaded files to the Classpath
         *
         * @param target file to be added to Classpath
         */
        private static void addClasspath(String target)
        {
            try
            {
                ((LaunchClassLoader) LLoader.class.getClassLoader()).addURL(new File(llibraryData, target).toURI().toURL());
            }
            catch (MalformedURLException e)
            {
                throw new RuntimeException(e);
            }
        }

        private static void removeClasspath(File mod)
        {
            if (mod.delete())
                return;

            try
            {
                ClassLoader cl = LLoader.class.getClassLoader();
                URL url = mod.toURI().toURL();
                Field f_ucp = URLClassLoader.class.getDeclaredField("ucp");
                Field f_loaders = URLClassPath.class.getDeclaredField("loaders");
                Field f_lmap = URLClassPath.class.getDeclaredField("lmap");
                f_ucp.setAccessible(true);
                f_loaders.setAccessible(true);
                f_lmap.setAccessible(true);

                URLClassPath ucp = (URLClassPath) f_ucp.get(cl);
                Closeable loader = ((Map<String, Closeable>) f_lmap.get(ucp)).remove(URLUtil.urlNoFragString(url));
                if (loader != null)
                {
                    loader.close();
                    ((List<?>) f_loaders.get(ucp)).remove(loader);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}