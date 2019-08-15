import java.security.MessageDigest; 
import java.util.Arrays;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.*;
import java.net.*;

public class AESEnc {
  static String IV = "AAAAAAAAAAAAAAAA";
  static String encryptionKey = "Hello World nexg";
  public static void main(String [] args) {
    try {
    InetAddress IPAddress = InetAddress.getByName("192.168.1.7");
    DatagramSocket clientSocket = new        DatagramSocket(5038,IPAddress);
    byte[] data=new byte[2048];
    DatagramPacket packet = new DatagramPacket(data, 2048);
    packet.setLength(2048); 
    System.out.println("Waiting for packet");
    clientSocket.receive(packet);
    String strMessage=new String(packet.getData(),0,packet.getLength());
    System.out.println("Recieved packet");
    System.out.println("==Java==");
    System.out.println("plain:   " + strMessage);
    String decrypted = decrypt(strMessage.getBytes(),encryptionKey);

} catch (Exception e) {
  e.printStackTrace();
} 
}

  public static byte[] encrypt(String plainText, String encryptionKey) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CTR/PKCS5Padding", "SunJCE");
    SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
    cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
    return cipher.doFinal(plainText.getBytes("UTF-8"));
  }

  public static String decrypt(byte[] cipherText, String encryptionKey) throws Exception{
    Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding", "SunJCE");
    SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
    cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
    return new String(cipher.doFinal(cipherText),"UTF-8");
  }
}
