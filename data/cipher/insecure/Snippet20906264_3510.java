public class Cryptooo {

SecretKeySpec key = null;
byte[] ciphertext;

 public void generateKey()  {

 String passphrase = "3xtr3meDiFficUltp@ss";

 MessageDigest digest = null;
 try {
     digest = MessageDigest.getInstance("SHA");
 } catch (NoSuchAlgorithmException e) {
     e.printStackTrace();
 }

 digest.update(passphrase.getBytes());
 key = new SecretKeySpec(digest.digest(), 0, 16, "AES");

 byte[] keyBytes = key.getEncoded();



 }


public byte[] encrypt(String string)    {
    Cipher aes = null;
    try {
        aes = Cipher.getInstance("AES/ECB/PKCS5Padding");

    aes.init(Cipher.ENCRYPT_MODE, key);
    ciphertext = aes.doFinal(string.getBytes());


    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    }

    return ciphertext;
}

public String decrypt(byte[] ciphertext)    {

    Cipher aes = null;
    String cleartext =null;


    try {
        aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
        aes.init(Cipher.DECRYPT_MODE, key);
        cleartext = new String(aes.doFinal(ciphertext));

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    }

    return cleartext;
}

}
