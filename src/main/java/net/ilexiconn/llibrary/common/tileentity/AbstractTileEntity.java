package net.ilexiconn.llibrary.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * @author iLexiconn
 * @since 0.1.0
 */
public abstract class AbstractTileEntity extends TileEntity {
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        saveClientDataToNBT(nbtTag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager manager, S35PacketUpdateTileEntity packet) {
        loadClientDataFromNBT(packet.func_148857_g());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTag) {
        super.readFromNBT(nbtTag);
        loadFromNBT(nbtTag);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTag) {
        super.writeToNBT(nbtTag);
        saveToNBT(nbtTag);
    }

    public abstract void loadFromNBT(NBTTagCompound nbtTag);

    public abstract void saveToNBT(NBTTagCompound nbtTag);

    public abstract void loadClientDataFromNBT(NBTTagCompound nbtTag);

    public abstract void saveClientDataToNBT(NBTTagCompound nbtTag);
}