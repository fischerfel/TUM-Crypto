package com.example.app;

import java.io.DataInputStream;
import java.net.URL;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;

public class MainClass {

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        try {
            BASE64Decoder decoder   = new BASE64Decoder();
            String b64PrivateKey    = getContents("http://localhost/api/keypair.php").trim();
            String b64EncryptedStr  = getContents("http://localhost/api/encrypt.php").trim();

            System.out.println("PrivateKey (b64): " + b64PrivateKey);
            System.out.println(" Encrypted (b64): " + b64EncryptedStr);

            SecretKeySpec privateKey    = new SecretKeySpec( decoder.decodeBuffer(b64PrivateKey) , "AES");
            Cipher cipher               = Cipher.getInstance("RSA/None/OAEPWithSHA1AndMGF1Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] plainText            = decoder.decodeBuffer(b64EncryptedStr);

            System.out.println("         Message: " + plainText);
        }
        catch( Exception e )
        {
            System.out.println("           Error: " + e.getMessage());
        }

    }

    public static String getContents(String url)
    {
        try {
            String result = "";
            String line;
            URL u = new URL(url);
            DataInputStream theHTML = new DataInputStream(u.openStream());
            while ((line = theHTML.readLine()) != null)
                result = result + "\n" + line;

            return result;
        }
        catch(Exception e){}

        return "";
    }
}
