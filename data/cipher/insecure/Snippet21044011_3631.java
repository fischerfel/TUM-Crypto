import java.security.*;

import javax.crypto.*;

import java.security.spec.*;

import javax.crypto.spec.*;

import javax.crypto.spec.IvParameterSpec;



public class myProgram
{
  public static void main (String[] args) throws Exception
  {

    String text = "Hello World;

    SecureRandom sr = new SecureRandom();
    byte [] iv = new byte[8];
    sr.nextBytes(iv);
    IvParameterSpec IV = new IvParameterSpec(iv);
    KeyGenerator kg = KeyGenerator.getInstance("DES");
    Key mykey = kg.generateKey();
    Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, mykey,IV);

    byte[] plaintext = text.getBytes("UTF8");



    byte[] ciphertext = cipher.doFinal(plaintext);

    System.out.println("\n\nCiphertext: ");
    for (int i=0;i<ciphertext.length;i++) {

        if (chkEight(i)) {
            System.out.print("\n");
        }
        System.out.print(ciphertext[i]+" ");
    }
