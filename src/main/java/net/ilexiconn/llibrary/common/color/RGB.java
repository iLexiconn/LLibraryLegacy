package net.ilexiconn.llibrary.common.color;

/**
 * @author RafaMv0
 * @since 0.1.0
 */
public class RGB {
    public int r, g, b, alpha;

    public RGB(int r, int g, int b, int alpha) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.alpha = alpha;
    }

    public boolean equals(Object obj) {
        if (obj instanceof RGB) {
            RGB rgb = (RGB) obj;

            return r == rgb.r && g == rgb.g && b == rgb.b && alpha == rgb.alpha;
        }

        return false;
    }

    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + r;
        hash = 53 * hash + g;
        hash = 53 * hash + b;
        hash = 53 * hash + alpha;
        return hash;
    }
}
