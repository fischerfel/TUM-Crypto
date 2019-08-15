import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
public class SimpleSymmetricPaddingExample{

public static void main(String[] args) throws Exception{
    String s = "HelloWorld";
    byte[] input = s.getBytes();

    byte[] keyBytes = {0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,
                          0x08,0x09,0x0a,0x0b,0x0c,0x0d,0x0e, 0x0f,
                          0x10,0x11,0x12,0x13,0x14,0x15,0x16,0x17};

    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

    SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

    System.out.println("input: " + new String(input));

    //encryption
    cipher.init(Cipher.ENCRYPT_MODE, key);

    byte[] cipherText = new byte[cipher.getOutputSize(input.length)];

    int ctLength = cipher.update(input, 0 , input.length, cipherText, 0);

    ctLength += cipher.doFinal(cipherText, ctLength);

    System.out.println("encrypted: " + new String(cipherText));

    //Decryption
    cipher.init(Cipher.DECRYPT_MODE, key);

    byte[] plainText = new byte[cipher.getOutputSize(cipherText.length)];

    int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);

    ptLength += cipher.doFinal(plainText, ptLength);
    System.out.println("decrypted: " + new String(plainText));
}

}
