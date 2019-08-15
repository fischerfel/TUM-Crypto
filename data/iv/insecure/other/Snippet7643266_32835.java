import java.math.*;
import javax.crypto.*;
import javax.crypto.spec.*;
public class TestCTR {
  static SecretKeySpec keySpec = new SecretKeySpec(new BigInteger("112233445566778899aabbccddeeff00", 16).toByteArray(), "AES");
  static IvParameterSpec ivSpec = new IvParameterSpec(new BigInteger("66778899aaffffffffffffffffffffff", 16).toByteArray());

  public static void main(String[] args) throws Exception {
    byte[] plaintext = new byte[256];
    for (int i = 0; i < 256; i++) plaintext[i] = (byte)i;

    // encrypt with CTR mode                                                                                                           
    byte[] ciphertext = new byte[256];
    Cipher c = Cipher.getInstance("AES/CTR/NoPadding");
    c.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
    c.doFinal(plaintext, 0, 256, ciphertext, 0);

    // decrypt, implementing CTR mode ourselves                                                                                        
    Cipher b = Cipher.getInstance("AES/ECB/NoPadding");
    b.init(Cipher.ENCRYPT_MODE, keySpec);
    for (int block = 0; block < 16; block++) {
      byte[] iv = ivSpec.getIV();
      int carry = 0;
      byte[] blockbytes = new byte[16];
      for (int i = 0; i < 4; i++) blockbytes[15 - i] = (byte)(block >> 8*i);
      for (int i = 15; i >= 0; i--) {
        int sum = (iv[i] & 255) + (blockbytes[i] & 255) + carry;
        iv[i] = (byte)sum;
        carry = sum >> 8;
      }
      b.doFinal(iv, 0, 16, iv, 0);
      for (int i = 0; i < 16; i++) plaintext[block*16+i] = (byte)(ciphertext[block*16+i] ^ iv[i]);
    }

    // check it                                                                                                                        
    for(int i = 0; i < 256; i++) assert plaintext[i] == (byte)i;
  }
}
