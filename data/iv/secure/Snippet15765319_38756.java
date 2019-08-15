import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoTest
{
  private class Slice
  {
    byte[] array;

    int offset;

    int size;
  }

  private Slice[] createSlices(int howMany, int whatSize)
  {
    Slice[] slices = new Slice[howMany];
    for (int i = 0; i < howMany; i++)
    {
        slices[i] = new Slice();
        slices[i].array = new byte[whatSize];
        slices[i].offset = 0;
        slices[i].size = 0;
    }

    return slices;
  }

  public void testWithOffsets() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, ShortBufferException,
                IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException
  {
    int num = 10;
    int size = 32784;
    Slice[] slices = createSlices(num, size);

    // Create the keys and parameters
    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    SecretKey key = keyGenerator.generateKey();

    SecretKey encryptionKey = new SecretKeySpec(key.getEncoded(), "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");

    cipher.init(Cipher.ENCRYPT_MODE, encryptionKey);

    // save IV
    byte iv[] = cipher.getIV();

    // setup input data
    for (int i = 0; i < num; i++)
    {
        slices[i].offset = 16;
        slices[i].size = 32768;
    }
    slices[num - 1].size = 5424;

    // calculate a sum of input data, for comparisson later
    Long sum = 0L;
    for (int i = 0; i < num; i++)
        for (int j = slices[i].offset; j < slices[i].offset + slices[i].size; j++)
            sum = sum + slices[i].array[j];

    // encrypt
    int finalSize = 0;
    for (int i = 0; i < num; i++)
    {
        if (i == num - 1)
            finalSize = cipher.doFinal(slices[i].array, slices[i].offset, slices[i].size, slices[i].array, slices[i].offset);
        else
            finalSize = cipher.update(slices[i].array, slices[i].offset, slices[i].size, slices[i].array, slices[i].offset);
        System.err.println("Original size is " + slices[i].size + " final size is " + finalSize);
        slices[i].size = finalSize;
    }

    // decrypt
    SecretKey decryptionKey = new SecretKeySpec(key.getEncoded(), "AES");
    Cipher decipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");

    IvParameterSpec ips = new IvParameterSpec(iv);
    decipher.init(Cipher.DECRYPT_MODE, decryptionKey, ips);

    for (int i = 0; i < num; i++)
    {
    /* ** */
        if (i == num - 1)
            finalSize = decipher.doFinal(slices[i].array, slices[i].offset, slices[i].size, slices[i].array, slices[i].offset);
        else
            finalSize = decipher.update(slices[i].array, slices[i].offset, slices[i].size, slices[i].array, slices[i].offset);
        System.err.println("Original size is " + slices[i].size + " final size is " + finalSize);
        slices[i].size = finalSize;
    }

    // sum of output data, should be the same as the input data
    Long newsum = 0L;
    for (int i = 0; i < num; i++)
        for (int j = slices[i].offset; j < slices[i].offset + slices[i].size; j++)
            newsum = newsum + slices[i].array[j];

    System.err.println("newsum " + newsum + " oldsum " + sum);
    assert newsum == sum;
  }

  public static void main(String[] args) throws Exception
  {
    CryptoTest ct = new CryptoTest();

    ct.testWithOffsets();
    ct.testWithoutOffsets();
  }
}
