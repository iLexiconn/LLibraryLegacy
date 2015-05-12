package net.ilexiconn.llibrary.common.color;

public class RGB
{
    public int r, g, b, alpha;

    public RGB(int r, int g, int b, int alpha)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.alpha = alpha;
    }

    public boolean equals(Object obj)
    {
        if (obj instanceof RGB)
        {
            RGB rgb = (RGB) obj;

            return r == rgb.r && g == rgb.g && b == rgb.b && alpha == rgb.alpha;
        }

        return false;
    }
}
