package net.ilexiconn.llibrary.common.update;

public enum UpdateType {
    RELEASE(0x8CAF62),
    BETA(0x7FA5C4),
    ALPHA(0xE49788);

    public final int color;

    UpdateType(int color) {
        this.color = color;
    }
}
