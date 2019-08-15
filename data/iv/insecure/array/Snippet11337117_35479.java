import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;

public class RandomKey
{
    public static void main(String[] args) throws Exception
    {
    byte[] input = new byte[] { 
                        0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 
                        0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
                        0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07 };


    byte[] ivBytes = new byte[] { 
                        0x00, 0x00, 0x00, 0x01, 0x04, 0x05, 0x06, 0x07,
                        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01 };

    //initializing a new initialization vector  
    IvParameterSpec ivSpec  = new IvParameterSpec(ivBytes);
    //what does this actually do?
    Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding", "BC");
    //what does this do?
    KeyGenerator generator = KeyGenerator.getInstance("AES","BC");
    //I assume this generates a key size of 192 bits
    generator.init(192);
    //does this generate a random key?
    Key encryptKey = generator.generateKey();

    System.out.println("input: " +toHex(input));

    //encryption phase
    cipher.init(Cipher.ENCRYPT_MODE, encryptKey, ivSpec);
    //what is this doing?
    byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
    //what is this doing?
    int ctLength = cipher.update(input, 0, input.length, cipherText,0);

    //getting the cipher text length i assume?
    ctLength += cipher.doFinal (cipherText, ctLength );
    System.out.println ("Cipher: " +toHex(cipherText) + " bytes: " + ctLength);


    //decryption phase
    cipher.init(Cipher.DECRYPT_MODE, encryptKey, ivSpec);
    //storing the ciphertext in plaintext i'm assuming?
    byte[] plainText = new byte[cipher.getOutputSize(ctLength)];
    int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);
    //getting plaintextLength i think?
    ptLength= cipher.doFinal (plainText, ptLength);
    System.out.println("plain: " + toHex(plainText, ptLength));  

    }

private static String digits = "0123456789abcdef";

public static String toHex(byte[] data, int length)
{
    StringBuffer buf = new StringBuffer();

    for (int i=0; i!= length; i++)
    {
        int v = data[i] & 0xff;

        buf.append(digits.charAt(v >>4));
        buf.append(digits.charAt(v & 0xf));
    }
    return buf.toString();

}

public static String toHex(byte[] data)
{
    return toHex(data, data.length);
}
}
