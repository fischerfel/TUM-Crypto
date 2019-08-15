import java.io.*;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;

import java.security.spec.KeySpec;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.PBEKeySpec;

import javax.crypto.BadPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.UnsupportedEncodingException;


public class sifrovaniRaw  {

Cipher sifr, desifr;



sifrovaniRaw(SecretKey klic, String algoritmus)
{
    try {
        sifr = Cipher.getInstance(algoritmus); 
        desifr = Cipher.getInstance(algoritmus);
        sifr.init(Cipher.ENCRYPT_MODE, klic);  
        desifr.init(Cipher.DECRYPT_MODE, klic);

    }
    catch (NoSuchPaddingException e) {
        System.out.println(" NoSuchPaddingException");
    }
    catch (NoSuchAlgorithmException e) {
        System.out.println(" NoSuchAlgorithmException");
    }
    catch (InvalidKeyException e) {
        System.out.println(" InvalidKeyException");
    }
}



sifrovaniRaw(String passph) {

    byte[] sul =
    {
        (byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,
        (byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99

    };


    int iterace = 20;

    try {

        KeySpec klicSpecifikace = new PBEKeySpec(passph.toCharArray(), sul, iterace);
        SecretKey klic = SecretKeyFactory.getInstance("PBEWithMD5AndTripleDES").generateSecret(klicSpecifikace);

        sifr = Cipher.getInstance(klic.getAlgorithm());
        desifr = Cipher.getInstance(klic.getAlgorithm());


        AlgorithmParameterSpec parametry = new PBEParameterSpec(sul, iterace);

        sifr.init(Cipher.ENCRYPT_MODE, klic, parametry);
        desifr.init(Cipher.DECRYPT_MODE, klic, parametry);
    }
    catch (InvalidAlgorithmParameterException e) {
        System.out.println(" InvalidAlgorithmParameterException");
    }
    catch (InvalidKeySpecException e) {
        System.out.println(" InvalidKeySpecException");
    }
    catch (NoSuchPaddingException e) {
        System.out.println(" NoSuchPaddingException");
    }
    catch (NoSuchAlgorithmException e) {
        System.out.println(" NoSuchAlgorithmException");
    }
    catch (InvalidKeyException e) {
        System.out.println(" InvalidKeyException");
    }
}


public String encryption(String str) {
    try {

        byte[] utf8 = str.getBytes("UTF8");


        byte[] enco = sifr.doFinal(utf8);


        return new sun.misc.BASE64Encoder().encode(enco);

    } catch (BadPaddingException e) {
    } catch (IllegalBlockSizeException e) {
    } catch (UnsupportedEncodingException e) {
    } catch (IOException e) {
    }
    return null;
}



public String decryption(String str) {

    try {


        byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
        byte[] utf8 = desifr.doFinal(dec);


        return new String(utf8, "UTF8");

    } catch (BadPaddingException e) {
    } catch (IllegalBlockSizeException e) {
    } catch (UnsupportedEncodingException e) {
    } catch (IOException e) {
    }
    return null;
}





public static void testsecklic()
{


      System.out.println("Testing method using secret key");
      System.out.println("");

      try {
          System.out.println("Give a path:");
          System.out.println("-----");
          System.out.println("for UNIX eg: \"/home/user/text_to_encrypt/decrypt.txt\"");
          System.out.println("for NT Windows eg: \"C://FileIO//text.txt\"");
          System.out.println("------");
            Scanner scan = new Scanner(System.in);
            String filepath = scan.next();
            File file = new File(filepath);
            BufferedInputStream bin = null;

            FileInputStream fin = new FileInputStream(file);
            bin = new BufferedInputStream(fin);
            byte[] contents = new byte[1024];
            int bytesRead=0;
            String StringCont;

            while( (bytesRead = bin.read(contents)) != -1)
            {
                StringCont = new String(contents, 0, bytesRead);
                System.out.print(StringCont);


        SecretKey aesKlic    = KeyGenerator.getInstance("AES").generateKey();
        SecretKey desKlic       = KeyGenerator.getInstance("DES").generateKey();
        SecretKey desedeKlic    = KeyGenerator.getInstance("DESede").generateKey();
        SecretKey rc2Klic    = KeyGenerator.getInstance("RC2").generateKey();
        SecretKey blowfishKlic  = KeyGenerator.getInstance("Blowfish").generateKey();

        StringEncrypter aesEncrypt = new StringEncrypter(aesKlic, aesKlic.getAlgorithm());
        StringEncrypter desEncrypt = new StringEncrypter(desKlic, desKlic.getAlgorithm());
        StringEncrypter desedeEncrypt = new StringEncrypter(desedeKlic, desedeKlic.getAlgorithm());
        StringEncrypter rc2Encrypt = new StringEncrypter(rc2Klic, rc2Klic.getAlgorithm());
        StringEncrypter blowfishEncrypt = new StringEncrypter(blowfishKlic, blowfishKlic.getAlgorithm());


        String aesEncrypted       = aesEncrypt.encrypt(StringCont);
        String desEncrypted       = desEncrypt.encrypt(StringCont);
        String rc2Encrypted       = desedeEncrypt.encrypt(StringCont);
        String desedeEncrypted    = rc2Encrypt.encrypt(StringCont);
        String blowfishEncrypted  = blowfishEncrypt.encrypt(StringCont);


        String aesDecrypt       = aesEncrypt.decrypt(aesEncrypted);
        String desDecrypt       = desEncrypt.decrypt(desEncrypted);
        String rc2Decrypt       = rc2Encrypt.decrypt(desedeEncrypted);
        String desedeDecrypt    = desedeEncrypt.decrypt(rc2Encrypted);
        String blowfishDecrypt  = blowfishEncrypt.decrypt(blowfishEncrypted);



        //FIRST NON-WORKING WAY of decrypting.
        System.out.println("Do you want to encrypt[1] or decrypt[2] text ?");
        int sifDesif = scan.nextInt();

        if(sifDesif == 1)
        {
            System.out.println("What cypher do you want to choose?");
            System.out.println("AES[1], DES[2], rc2[3], blowfish[4], desede[5]");
            int vyber = scan.nextInt();

            switch(vyber)
            {
                case 1: System.out.println("encrypted : " + aesEncrypted); break;
                case 2: System.out.println("encrypted : " + desEncrypted); break;
                case 3: System.out.println("encrypted : " + rc2Encrypted); break;
                case 4: System.out.println("encrypted : " + blowfishEncrypted); break;
                case 5: System.out.println("encrypted : " + desedeEncrypted); break;
            }

        }
        else if(sifDesif == 2)
        {
            System.out.println("What cypher do you want to choose?");
            System.out.println("AES[1], DES[2], rc2[3], blowfish[4], desede[5]");
            int vyber = scan.nextInt();

            switch(vyber)
            {
                case 1:System.out.println("decrypted : " + aesDecrypt); break;
                case 2:System.out.println("decrypted : " + desDecrypt); break;
                case 3:System.out.println("decrypted : " + rc2Decrypt); break;
                case 4:System.out.println("decrypted : " + blowfishDecrypt); break;
                case 5:System.out.println("decrypted : " + desedeDecrypt); break;
            }
        }
        //SECOND WORKING WAY of decrypting.
        System.out.println("Algorith used:" + aesKlic.getAlgorithm());
        System.out.println("text  : " + StringCont);
        System.out.println("encryption : " + aesEncrypted);
        System.out.println("decryption text : " + aesDecrypt);
        System.out.println();



       }
    }
    catch (NoSuchAlgorithmException e)
    {
        System.out.println(" No such Algo");
    }
        catch(FileNotFoundException e)
    {
        System.out.println("File not found" + e);
    }
    catch(IOException ioe)
    {
        System.out.println("Exception while reading the file "+ ioe);
    }
}


public static void main(String[] args)
{
    testsecklic();
}
