package org.golde.java.game.helpers;

/**
 * HSV color class
 * @author Eric
 *
 */
public class HsvColor
{
    public final int red, green, blue;
    public float r, g, b;
    public int getColorValue(){
    	return red * 0x10000 + green * 0x100 + blue + 0xff000000; 
    }
    /**
     * Get a color based on HSV
     * @param h Hue
     * @param s Saturation
     * @param v Value
     */
    public HsvColor(float h, float s, float v)
    {
        int i;
        float f, p, q, t;
        
        if (s == 0) {
            // achromatic (grey)
            r = g = b = v;

        }
        else {
            if (h >= 1)
                h -= 1;
            h *= 6;         // sector 0 to 5
            i = (int)Math.floor(h);
            f = h - i;          // factorial part of h
            p = v * (1 - s);
            q = v * (1 - s * f);
            t = v * (1 - s * (1 - f));
            switch (i) {
                case 0:
                    r = v;
                    g = t;
                    b = p;
                    break;
                case 1:
                    r = q;
                    g = v;
                    b = p;
                    break;
                case 2:
                    r = p;
                    g = v;
                    b = t;
                    break;
                case 3:
                    r = p;
                    g = q;
                    b = v;
                    break;
                case 4:
                    r = t;
                    g = p;
                    b = v;
                    break;
                default:        // case 5:
                    r = v;
                    g = p;
                    b = q;
                    break;
            }
        }

        red = (int)Math.round(r * 255.0);
        green = (int)Math.round(g * 255.0);
        blue = (int)Math.round(b * 255.0);
    }
}
