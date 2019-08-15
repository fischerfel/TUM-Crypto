/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tbn;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.imageio.ImageIO;

/**
 *
 * @author user
 */
public class DbtClass {

    public static void main(String[] args) {
        try {
            BufferedImage orgnlimage = ImageIO.read(new File("parrruuuuu.png"));
            orgnlimage = user_space(orgnlimage);
            byte[] orgnlimagebytes = get_byte_data(orgnlimage);
            byte[] encryptedbytes = encrypt(orgnlimagebytes, "abc");
            BufferedImage encryptedimage = toImage(encryptedbytes, orgnlimage.getWidth(), orgnlimage.getHeight());
            ImageIO.write(encryptedimage, "png", new File("encrypted.png"));

            /////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////

            byte[] encryptedbytes2 = get_byte_data(encryptedimage);
            System.out.println("encryptedbytes before writing: "+encryptedbytes2.length);

            BufferedImage encryptedimage3 = ImageIO.read(new File("encrypted.png"));
            byte[] encryptedbyte3 = get_byte_data(encryptedimage3);
            System.out.println("encryptedbytes after writing: "+encryptedbyte3.length);


        } catch (IOException ex) {
            Logger.getLogger(DbtClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static BufferedImage user_space(BufferedImage image) {
        //create new_img with the attributes of image
        BufferedImage new_img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = new_img.createGraphics();
        graphics.drawRenderedImage(image, null);
        graphics.dispose(); //release all allocated memory for this image
        return new_img;
    }

    public static byte[] get_byte_data(BufferedImage image) {
        WritableRaster raster = image.getRaster();
        DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();
        return buffer.getData();
    }

    public static byte[] encrypt(byte[] orgnlbytes, String key) {
        byte[] encbytes = null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            // cryptograph. secure random 
            random.setSeed(key.getBytes());

            keyGen.init(128, random);
            // for example
            SecretKey secretKey = keyGen.generateKey();
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            encbytes = cipher.doFinal(orgnlbytes);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DbtClass.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(DbtClass.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(DbtClass.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(DbtClass.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(DbtClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return encbytes;
    }

    public static BufferedImage toImage(byte[] imagebytes, int width, int height) {
        DataBuffer buffer = new DataBufferByte(imagebytes, imagebytes.length);
        WritableRaster raster = Raster.createInterleavedRaster(buffer, width, height, 3 * width, 3, new int[]{2, 1, 0}, (Point) null);
        ColorModel cm = new ComponentColorModel(ColorModel.getRGBdefault().getColorSpace(), false, true, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        return new BufferedImage(cm, raster, true, null);
    }
}
