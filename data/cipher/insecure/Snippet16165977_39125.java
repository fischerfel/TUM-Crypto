public class DesEncryptor {
private static Key key;
private static Cipher cipher;

public static void keyGenerate() throws NoSuchAlgorithmException, NoSuchPaddingException{
    // get a DES cipher object and print the provider
    cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
    KeyGenerator keyGen = KeyGenerator.getInstance("DES");
    keyGen.init(64);
    key = keyGen.generateKey();

}

public static String encryptSms(String sms) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, 
IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{



    //System.out.println( "\n" + cipher.getProvider().getInfo() );
    // encrypt using the key and the plaintext
    //  System.out.println( "\nStart encryption" );
    cipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] cipherText = cipher.doFinal(sms.getBytes("UTF8"));
    // System.out.println( "Finish encryption: " );
    return( new String(cipherText, "UTF8") );
}

public static String decryptSms(String smsEncrypted) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, 
UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException
{
    // decrypt the ciphertext using the same key
    //System.out.println( "\nStart decryption" );

    cipher.init(Cipher.DECRYPT_MODE, key);
    byte[] newPlainText = cipher.doFinal(smsEncrypted.getBytes());
    //System.out.println( "Finish decryption: " );

    return( new String(newPlainText, "UTF8") );
}
