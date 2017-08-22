package org.golde.java.game.helpers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.golde.java.game.GLog;

/**
 * Loads PNG Images as ByteBuffers to display as Icons on the OS
 * @author Eric
 *
 */
public class IconLoader
{
 
	/**
	 * Turns a file into a ByteBuffer[] for OS
	 * @param icon Icon file
	 * @return ByteBuffer[] for OS
	 */
    public static ByteBuffer[] load(File icon)
    {
        BufferedImage image = null;
        try
        {
            image = ImageIO.read(icon);
        }
        catch (IOException e)
        {
        	GLog.error(e, "Failed to load file icon! Does it exist?");
        }
        ByteBuffer[] buffers = null;
        String OS = System.getProperty("os.name").toUpperCase();
        if(OS.contains("WIN"))
        {
            buffers = new ByteBuffer[2];
            buffers[0] = loadInstance(image, 16);
            buffers[1] = loadInstance(image, 32);
        }
        else if(OS.contains("MAC"))
        {
            buffers = new ByteBuffer[1];
            buffers[0] = loadInstance(image, 128);
        }
        else
        {
            buffers = new ByteBuffer[1];
            buffers[0] = loadInstance(image, 32);
        }
        return buffers;
    }
 
    private static ByteBuffer loadInstance(BufferedImage image, int dimension)
    {
        BufferedImage scaledIcon = new BufferedImage(dimension, dimension,
                BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D g = scaledIcon.createGraphics();
        double ratio = getIconRatio(image, scaledIcon);
        double width = image.getWidth() * ratio;
        double height = image.getHeight() * ratio;
        g.drawImage(image, (int) ((scaledIcon.getWidth() - width) / 2),
                (int) ((scaledIcon.getHeight() - height) / 2), (int) (width),
                (int) (height), null);
        g.dispose();
 
        return convertToByteBuffer(scaledIcon);
    }
 

    private static double getIconRatio(BufferedImage src, BufferedImage icon)
    {
        double ratio = 1;
        if (src.getWidth() > icon.getWidth())
            ratio = (double) (icon.getWidth()) / src.getWidth();
        else
            ratio = (int) (icon.getWidth() / src.getWidth());
        if (src.getHeight() > icon.getHeight())
        {
            double r2 = (double) (icon.getHeight()) / src.getHeight();
            if (r2 < ratio)
                ratio = r2;
        }
        else
        {
            double r2 = (int) (icon.getHeight() / src.getHeight());
            if (r2 < ratio)
                ratio = r2;
        }
        return ratio;
    }
    
    /**
     * Turn BufferedImage into ByteBuffer for OS
     * @param image BufferedImage input
     * @return ByteBuffer for OS
     */
    public static ByteBuffer convertToByteBuffer(BufferedImage image)
    {
        byte[] buffer = new byte[image.getWidth() * image.getHeight() * 4];
        int counter = 0;
        for (int i = 0; i < image.getHeight(); i++)
            for (int j = 0; j < image.getWidth(); j++)
            {
                int colorSpace = image.getRGB(j, i);
                buffer[counter + 0] = (byte) ((colorSpace << 8) >> 24);
                buffer[counter + 1] = (byte) ((colorSpace << 16) >> 24);
                buffer[counter + 2] = (byte) ((colorSpace << 24) >> 24);
                buffer[counter + 3] = (byte) (colorSpace >> 24);
                counter += 4;
            }
        return ByteBuffer.wrap(buffer);
    }
}
