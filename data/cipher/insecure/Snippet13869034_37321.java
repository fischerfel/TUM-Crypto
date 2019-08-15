/* class for crypting and decrypting a file */
class DESEncrypter
{
private Cipher encryptionCipher;
private Cipher decryptionCipher;

public DESEncrypter (SecretKey key) throws Exception
{
encryptionCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
encryptionCipher.init(Cipher.ENCRYPT_MODE, key);
decryptionCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
decryptionCipher.init(Cipher.DECRYPT_MODE, key);
}

/* write to 'out' the encryption of the information read from 'in' */
public String encrypt(String unencryptedString)
{
    String encryptedString = "";

    try {
        byte[] unencryptedByteArray = unencryptedString.getBytes("UTF8");

        byte[] encryptedBytes = this.encryptionCipher.doFinal(unencryptedByteArray);

        encryptedString = new sun.misc.BASE64Encoder().encode(encryptedBytes);

    } catch (Exception ex) {
        Logger.getLogger(DESEncrypter.class.getName()).log(Level.SEVERE, null, ex);
    }

    return encryptedString;
}

private static String bytes2String(byte[] bytes)
{

    StringBuffer stringBuffer = new StringBuffer();
    for (int i = 0; i < bytes.length; i++) 
    {
        stringBuffer.append((char) bytes[i]);
    }

    return stringBuffer.toString();
}

/* write to 'out' the information obtained by decrypting the information read from 'in' */
public String decrypt (String encryptedString) throws UnsupportedEncodingException
{
    byte[] unencryptedByteArray = new byte[4096];

    try {
        // Encode bytes to base64 to get a string
        byte[] decodedBytes = new sun.misc.BASE64Decoder().decodeBuffer(encryptedString);

       // Decrypt
       unencryptedByteArray = this.decryptionCipher.doFinal(decodedBytes);     
    } catch (Exception ex) {
        Logger.getLogger(DESEncrypter.class.getName()).log(Level.SEVERE, null, ex);
    }

    return bytes2String(unencryptedByteArray);
}
} 
