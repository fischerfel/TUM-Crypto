package org.temp2.cod1;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;

public class Code1 {

    byte[] plaintext = new byte[32];   // <<<<<<<<<<<<<<<<<<<<<<<<<< syntax error
    for (int i = 0; i < 32; i++) {
      plaintext[i] = (byte) (i % 16);
    }

    byte[] key = new byte[16];
    SecureRandom r = new SecureRandom();
    r.nextBytes(key);

    Cipher c = Cipher.getInstance("AES");
    SecretKeySpec k =  new SecretKeySpec(key, "AES");
    c.init(Cipher.ENCRYPT_MODE, k);
    byte[] encryptedData = c.doFinal(plaintext);
}
}
