package com.decryption;

import java.io.*;
import java.math.BigInteger;

import java.security.*;
import java.security.spec.*;
import java.security.interfaces.*;

import javax.crypto.*;
import javax.crypto.spec.*;
import javax.crypto.interfaces.*;

public class RSADecrypt
{
   public RSADecrypt(String inFileName, String outFileName) {


      try {
          System.out.println("Inside TRY");
         /* Get the encrypted message from file. */
         FileInputStream cipherfile = new FileInputStream(inFileName);
         byte[] ciphertext = new byte[cipherfile.available()];
         cipherfile.read(ciphertext);
         cipherfile.close();
         System.out.println("Inside 1");
         /* Get the private key from file. */
         //PrivateKey privatekey = readPrivateKey("D://sso//mmdevnopass.key");
         PrivateKey privatekey = readPrivateKey("D://sso//mmdevJWE.key");
         System.out.println("Inside 2");

         /* Create cipher for decryption. */
         Cipher decrypt_cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
         decrypt_cipher.init(Cipher.DECRYPT_MODE, privatekey);
         System.out.println("Inside 3");
         /* Reconstruct the plaintext message. */
         byte[] plaintext = decrypt_cipher.doFinal(ciphertext);
         FileOutputStream plainfile = new FileOutputStream(outFileName);
         plainfile.write(plaintext);
         plainfile.close();
      } catch (Exception e) {
          System.out.println("catch1");
         e.printStackTrace();
      }
   }

   public static PrivateKey readPrivateKey(String filename) throws Exception {
       System.out.println("readPrivateKey()");
      FileInputStream file = new FileInputStream(filename);
      byte[] bytes = new byte[file.available()];
      file.read(bytes);
      file.close();
      System.out.println("readPrivateKey() 1");
      PKCS8EncodedKeySpec privspec = new PKCS8EncodedKeySpec(bytes);
     // X509EncodedKeySpec privspec= new X509EncodedKeySpec(bytes);
      //RSAPrivateKeySpec privspec = new RSAPrivateKeySpec(modulus, privateExponent)
      System.out.println("readPrivateKey() 2");
      KeyFactory factory = KeyFactory.getInstance("RSA");
      System.out.println("readPrivateKey() 3");
      PrivateKey privkey = factory.generatePrivate(privspec);
      System.out.println("readPrivateKey() 4");
      return privkey;
   }

   public static void main(String[] arg) {
      /*if (arg.length != 2) {
         System.err.println("Usage:  java RSADecrypt <src file> <dest file>");
      } else {*/
       System.out.println("Welcome");
       String inFileName="D://sso//myJEK.txt";
       String outFileName="D://sso//out.txt";
         new RSADecrypt(inFileName,outFileName);
     // }
   }
}
