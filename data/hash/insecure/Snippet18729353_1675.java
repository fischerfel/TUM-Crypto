package com.jthink.songlayer;

import com.jthink.songlayer.utils.Base64Coder;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.envers.Audited;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.stream.ImageInputStream;
import javax.persistence.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.CharBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  An Image
 */
@Audited
@Entity
public class CoverImage
{

    public CoverImage()
    {

    }

    public CoverImage(byte[] imageData)
    {
        this.imageData=imageData;
    }

    @Id
    @Column(length = 1000)
    private String dataKey;

    @Version
    private int version;

    public String getDataKey()
    {
        return dataKey;
    }

    public void setDataKey(String dataKey)
    {
        this.dataKey = dataKey;
    }

    @Lob
    private byte[]  imageData;

    @Lob
    private byte[]  thumbnailData;

    private String  mimeType;
    private int     width;
    private int     height;
    private boolean isLinked;

    @org.hibernate.annotations.Index(name = "IDX_SOURCE")
    private String  source;

    @Lob
    private byte[]  resizedImageData;
    private int     resizedWidth;
    private int     resizedHeight;

    public byte[] getImageData()
    {
        return imageData;
    }

    public void setImageData(byte[] imageData)
    {
        this.imageData = imageData;
    }

    public byte[] getThumbnailData()
    {
        return thumbnailData;
    }

    public void setThumbnailData(byte[] thumbnailData)
    {
        this.thumbnailData = thumbnailData;
    }

    public String getMimeType()
    {
        return mimeType;
    }

    public void setMimeType(String mimeType)
    {
        this.mimeType = mimeType;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public boolean isLinked()
    {
        return isLinked;
    }

    public void setLinked(boolean linked)
    {
        isLinked = linked;
    }


    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public byte[] getResizedImageData()
    {
        return resizedImageData;
    }

    public void setResizedImageData(byte[] resizedImageData)
    {
        this.resizedImageData = resizedImageData;
    }

    public int getResizedWidth()
    {
        return resizedWidth;
    }

    public void setResizedWidth(int resizedWidth)
    {
        this.resizedWidth = resizedWidth;
    }

    public int getResizedHeight()
    {
        return resizedHeight;
    }

    public void setResizedHeight(int resizedHeight)
    {
        this.resizedHeight = resizedHeight;
    }

    /**
     * Create message digest of the byte data
     * <p/>
     * This uniquely identifies the imagedata, but takes up much less room than the original data
     *
     * @param imageData
     * @return
     */
    public static byte[] getImageDataDigest(byte[] imageData)
    {
        //Calculate checksum
        MessageDigest md;
        try
        {
            md = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException nsae)
        {
            //This should never happen
            throw new RuntimeException(nsae);
        }

        md.reset();
        md.update(imageData);
        return md.digest();
    }

    public static String createKeyFromData(byte[] imageData)
    {
        try
        {
            String base64key = CharBuffer.wrap(Base64Coder.encode(getImageDataDigest(imageData))).toString();
            return base64key;
        }
        catch (NullPointerException npe)
        {
            throw new RuntimeException("Unable to create filename from sum");
        }
    }
}
