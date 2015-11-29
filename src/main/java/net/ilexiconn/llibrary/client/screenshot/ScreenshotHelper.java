package net.ilexiconn.llibrary.client.screenshot;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class used to replace the default screenshot behaviour.
 *
 * @author iLexiconn
 * @since 0.2.0
 */
public class ScreenshotHelper implements Runnable {
    private static IntBuffer pixelBuffer;
    private static int[] pixelValues;
    private int width;
    private int height;
    private String captureTime;
    private int[] pixels;
    private Framebuffer frameBuffer;

    public static void saveScreenshotAsync(int width, int height, int[] pixels, Framebuffer frameBuffer) {
        ScreenshotHelper saver = new ScreenshotHelper();
        saver.width = width;
        saver.height = height;
        saver.captureTime = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date());
        saver.pixels = pixels;
        saver.frameBuffer = frameBuffer;
        new Thread(saver).start();
    }

    public static void takeScreenshot() {
        Minecraft mc = Minecraft.getMinecraft();
        Framebuffer frameBuffer = mc.getFramebuffer();
        int screenshotWidth = mc.displayWidth;
        int screenshotHeight = mc.displayHeight;

        if (OpenGlHelper.isFramebufferEnabled()) {
            screenshotWidth = frameBuffer.framebufferTextureWidth;
            screenshotHeight = frameBuffer.framebufferTextureHeight;
        }

        int targetCapacity = screenshotWidth * screenshotHeight;

        if (ScreenshotHelper.pixelBuffer == null || ScreenshotHelper.pixelBuffer.capacity() < targetCapacity) {
            ScreenshotHelper.pixelBuffer = BufferUtils.createIntBuffer(targetCapacity);
            ScreenshotHelper.pixelValues = new int[targetCapacity];
        }

        GL11.glPixelStorei(3333, 1);
        GL11.glPixelStorei(3317, 1);
        ScreenshotHelper.pixelBuffer.clear();

        if (OpenGlHelper.isFramebufferEnabled()) {
            GL11.glBindTexture(3553, frameBuffer.framebufferTexture);
            GL11.glGetTexImage(3553, 0, 32993, 33639, ScreenshotHelper.pixelBuffer);
        } else {
            GL11.glReadPixels(0, 0, screenshotWidth, screenshotHeight, 32993, 33639, ScreenshotHelper.pixelBuffer);
        }

        ScreenshotHelper.pixelBuffer.get(ScreenshotHelper.pixelValues);
        TextureUtil.processPixelValues(ScreenshotHelper.pixelValues, screenshotWidth, screenshotHeight);
        int[] pixelCopy = new int[ScreenshotHelper.pixelValues.length];
        System.arraycopy(ScreenshotHelper.pixelValues, 0, pixelCopy, 0, ScreenshotHelper.pixelValues.length);
        ScreenshotHelper.saveScreenshotAsync(screenshotWidth, screenshotHeight, pixelCopy, frameBuffer);
    }

    @Override
    public void run() {
        BufferedImage image;

        if (OpenGlHelper.isFramebufferEnabled()) {
            image = new BufferedImage(frameBuffer.framebufferWidth, frameBuffer.framebufferHeight, 1);
            int i;
            for (int diff = i = frameBuffer.framebufferTextureHeight - frameBuffer.framebufferHeight; i < frameBuffer.framebufferTextureHeight; ++i) {
                for (int j = 0; j < frameBuffer.framebufferWidth; ++j) {
                    int pixel = pixels[i * frameBuffer.framebufferTextureWidth + j];
                    image.setRGB(j, i - diff, pixel);
                }
            }
        } else {
            image = new BufferedImage(width, height, 1);
            image.setRGB(0, 0, width, height, pixels, 0, width);
        }

        File ssDir = new File("screenshots");
        File ssFile = new File("screenshots", captureTime + ".png");
        for (int iterator = 0; ssFile.exists(); ssFile = new File("screenshots", captureTime + "_" + iterator + ".png")) {
            ++iterator;
        }

        try {
            ssDir.mkdirs();
            ImageIO.write(image, "png", ssFile);
            FMLClientHandler.instance().getClient().thePlayer.addChatComponentMessage(new ChatComponentText("Saved screenshot as ").appendSibling(new ChatComponentText(ssFile.getName()).setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, ssDir.getAbsolutePath())).setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(StatCollector.translateToLocal("gui.llibrary.screenshot")))).setUnderlined(true))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
