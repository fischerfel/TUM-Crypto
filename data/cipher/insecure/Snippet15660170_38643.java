package com.example.app;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.StringReader;
import java.net.URL;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Security;

import javax.crypto.Cipher;

import org.bouncycastle.openssl.PEMReader;

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

            byte[] decodedKey           = decoder.decodeBuffer(b64PrivateKey);
            byte[] decodedStr           = decoder.decodeBuffer(b64EncryptedStr);
            PrivateKey privateKey       = strToPrivateKey(new String(decodedKey));

            Cipher cipher               = Cipher.getInstance("RSA/None/OAEPWithSHA1AndMGF1Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);


            byte[] plainText            = cipher.doFinal(decodedStr);

            System.out.println("         Message: " + new String(plainText));
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

    public static PrivateKey strToPrivateKey(String s)
    {
        try {
            BufferedReader br   = new BufferedReader( new StringReader(s) );
            PEMReader pr        = new PEMReader(br);
            KeyPair kp          = (KeyPair)pr.readObject();
            pr.close();
            return kp.getPrivate();
        }
        catch( Exception e )
        {

        }

        return null;
    }
}
