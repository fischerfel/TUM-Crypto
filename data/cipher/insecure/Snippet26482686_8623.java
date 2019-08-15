import java.util.*;
import java.lang.*;
import java.io.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

class Ideone
{
    public static void main(String[] args) throws Exception {
    blowfishEncrypt("plaintextfile", "ciphertextfile");
  }

  public static void blowfishEncrypt(String f1, String f2) throws Exception {
    String keytext = "mykey";
    SecretKey secret_key = new SecretKeySpec(keytext.getBytes(), "Blowfish");
    Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secret_key);

    BufferedInputStream in = new BufferedInputStream(new FileInputStream(f1));
    CipherOutputStream out = new CipherOutputStream(new BufferedOutputStream(new FileOutputStream(
        f2)), cipher);
    int i;
    do {
      i = in.read();
      if (i != -1)
        out.write(i);
    } while (i != -1);
    in.close();
    out.close();
  }
}
