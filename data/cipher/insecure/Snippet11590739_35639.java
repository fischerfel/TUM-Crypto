public class AES {

private static final String algo="AES";
private static final byte[] keyValue= 
        new byte[]{somekey};

private static Key generateKey() throws Exception{

    Key key= new SecretKeySpec(keyValue, algo);

    return key;
}

public static String encrypt(String  email) throws Exception{

    Key key=generateKey();
    Cipher c=Cipher.getInstance(algo);
    c.init(Cipher.ENCRYPT_MODE, key);
    byte[] encVal=c.doFinal(email.getBytes());
    String encryptedEmail= new BASE64Encoder().encode(encVal);

    return encryptedEmail;
}

public static String decrypt(String encryptedEmail) throws Exception{

    Key key=generateKey();
    Cipher c=Cipher.getInstance(algo);
    c.init(Cipher.DECRYPT_MODE, key);

    byte[] decodeEmail= new BASE64Decoder().decodeBuffer(encryptedEmail);
    byte[] decodedEmail=c.doFinal(decodeEmail);

    String decryptedEmail= new String(decodedEmail);

    return decryptedEmail;
    }
