package mani.droid.browsedropbox;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypter {

    Cipher encipher;
    Cipher decipher;
CipherInputStream cis;
CipherOutputStream cos;
FileInputStream fis;
byte[] ivbytes = new byte[]{(byte)'a', (byte)'b', (byte)'c', (byte)'d', (byte)'e',                                                                                          (byte)'f', (byte)'g', (byte)'h', (byte)'i', (byte)'j', (byte)'k', (byte)'l', (byte)'m',     (byte)'n', (byte)'o', (byte)'p'};
    IvParameterSpec iv = new IvParameterSpec(ivbytes);

public boolean enCrypt(String key, InputStream is, OutputStream os)
{
    try {
        byte[] encoded = new BigInteger(key, 16).toByteArray();
        SecretKey seckey = new SecretKeySpec(encoded, "AES");
        encipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        encipher.init(Cipher.ENCRYPT_MODE, seckey, iv);
        cis = new CipherInputStream(is, encipher);
        copyByte(cis, os);
        return true;
    } 
    catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return false;
}

public boolean deCrypt(String key, InputStream is, OutputStream os)
{
    try {
        byte[] encoded = new BigInteger(key, 16).toByteArray();
        SecretKey seckey = new SecretKeySpec(encoded, "AES");
        encipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        encipher.init(Cipher.DECRYPT_MODE, seckey, iv);
        cos = new CipherOutputStream(os, encipher);
        copyByte(is, cos);
        //cos.close();
        return true;
    } 
    catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return false;
}

public void copyByte(InputStream is, OutputStream os) throws IOException
{
    byte[] buf = new byte[8192];
    int numbytes;
    while((numbytes = is.read(buf)) != -1)
    {
        os.write(buf, 0, numbytes);
        os.flush();
    }
    os.close();
    is.close();
}
}
