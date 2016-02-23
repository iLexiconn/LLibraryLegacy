package net.ilexiconn.llibrary.client.matrix;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class StackUnderflowError extends Error {
    private static final long serialVersionUID = -6946629885006358454L;

    public StackUnderflowError() {
        super();
    }

    public StackUnderflowError(String s) {
        super(s);
    }
}
