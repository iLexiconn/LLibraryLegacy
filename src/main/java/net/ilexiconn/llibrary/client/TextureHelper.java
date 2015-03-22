package net.ilexiconn.llibrary.client;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.StringUtils;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

/**
 * Player skin tools, not stable and only works in 1.7.10.
 *
 * @author iLexiconn
 */
@SideOnly(Side.CLIENT)
public class TextureHelper
{
    private static Minecraft mc = Minecraft.getMinecraft();

    public static BufferedImage getPlayerSkin(AbstractClientPlayer player)
    {
        BufferedImage bufferedImage = null;
        InputStream inputStream = null;
        Map map = mc.func_152342_ad().func_152788_a(player.getGameProfile());
        ITextureObject texture;

        try
        {
            if (map.containsKey(Type.SKIN))
                texture = mc.renderEngine.getTexture(mc.func_152342_ad().func_152792_a((MinecraftProfileTexture) map.get(Type.SKIN), Type.SKIN));
            else texture = mc.renderEngine.getTexture(player.getLocationSkin());

            if (texture instanceof ThreadDownloadImageData)
            {
                bufferedImage = ObfuscationReflectionHelper.getPrivateValue(ThreadDownloadImageData.class, (ThreadDownloadImageData) texture, "field_110560_d", "bufferedImage");
            }
            else if (texture instanceof DynamicTexture)
            {
                int width = ObfuscationReflectionHelper.getPrivateValue(DynamicTexture.class, (DynamicTexture) texture, "field_94233_j", "width");
                int height = ObfuscationReflectionHelper.getPrivateValue(DynamicTexture.class, (DynamicTexture) texture, "field_94234_k", "height");
                bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                bufferedImage.setRGB(0, 0, width, height, ((DynamicTexture) texture).getTextureData(), 0, width);
            }
            else
            {
                inputStream = mc.getResourceManager().getResource(AbstractClientPlayer.locationStevePng).getInputStream();
                bufferedImage = ImageIO.read(inputStream);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            IOUtils.closeQuietly(inputStream);
        }

        return bufferedImage;
    }

    public static void setPlayerSkin(AbstractClientPlayer entityPlayer, BufferedImage skin)
    {
        if (!hasBackup(entityPlayer)) backupPlayerSkin(entityPlayer);
        uploadPlayerSkin(entityPlayer, skin);
    }

    public static void resetPlayerSkin(AbstractClientPlayer entityPlayer)
    {
        BufferedImage image = getOriginalPlayerSkin(entityPlayer);
        if (image != null) uploadPlayerSkin(entityPlayer, image);
    }

    public static boolean hasBackup(AbstractClientPlayer player)
    {
        return new File("llibrary" + File.separator + "skin-backups" + File.separator + player.getCommandSenderName() + ".png").exists();
    }

    private static void backupPlayerSkin(AbstractClientPlayer entityPlayer)
    {
        BufferedImage bufferedImage = getPlayerSkin(entityPlayer);

        File file = new File("llibrary" + File.separator + "skin-backups");
        file.mkdir();
        File skinFile = new File(file, entityPlayer.getCommandSenderName() + ".png");
        try
        {
            skinFile.createNewFile();
            if (bufferedImage != null) ImageIO.write(bufferedImage, "PNG", skinFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void uploadPlayerSkin(AbstractClientPlayer player, BufferedImage bufferedImage)
    {
        ITextureObject textureObject = Minecraft.getMinecraft().renderEngine.getTexture(player.getLocationSkin());

        if (textureObject == null)
        {
            textureObject = new ThreadDownloadImageData(null, String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", StringUtils.stripControlCodes(player.getCommandSenderName())), AbstractClientPlayer.locationStevePng, new ImageBufferDownload());
            Minecraft.getMinecraft().renderEngine.loadTexture(player.getLocationSkin(), textureObject);
        }

        uploadTexture(textureObject, bufferedImage);
    }

    private static BufferedImage getOriginalPlayerSkin(AbstractClientPlayer entityPlayer)
    {
        File file = new File("llibrary" + File.separator + "skin-backups" + File.separator + entityPlayer.getCommandSenderName() + ".png");
        BufferedImage bufferedImage = null;

        try
        {
            if (file.exists()) bufferedImage = ImageIO.read(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return bufferedImage;
    }

    private static void uploadTexture(ITextureObject textureObject, BufferedImage bufferedImage)
    {
        TextureUtil.uploadTextureImage(textureObject.getGlTextureId(), bufferedImage);
    }
}
