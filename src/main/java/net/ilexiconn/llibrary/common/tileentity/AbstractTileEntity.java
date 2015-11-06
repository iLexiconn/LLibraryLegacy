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
    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        saveClientDataToNBT(nbtTag);
        return new S35PacketUpdateTileEntity(pos, 0, nbtTag);
    }

    public void onDataPacket(NetworkManager manager, S35PacketUpdateTileEntity packet) {
        loadClientDataFromNBT(packet.getNbtCompound());
    }

    public void readFromNBT(NBTTagCompound nbtTag) {
        super.readFromNBT(nbtTag);
        loadFromNBT(nbtTag);
    }

    public void writeToNBT(NBTTagCompound nbtTag) {
        super.writeToNBT(nbtTag);
        saveToNBT(nbtTag);
    }

    public abstract void loadFromNBT(NBTTagCompound nbtTag);

    public abstract void saveToNBT(NBTTagCompound nbtTag);

    public abstract void loadClientDataFromNBT(NBTTagCompound nbtTag);

    public abstract void saveClientDataToNBT(NBTTagCompound nbtTag);
}