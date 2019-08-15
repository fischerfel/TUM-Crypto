public class SEncryption {

private static final String ALGO = "AES";
private static final byte[] keyValue = new byte[] { 'O', 'u', 'r', 'B',
'e', 's', 't','S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y' };

public static String encrypt(String Data) throws Exception {
Key key = generateKey();
Cipher c = Cipher.getInstance(ALGO);
c.init(Cipher.ENCRYPT_MODE, key);
byte[] encVal = c.doFinal(Data.getBytes());
Base64 ob=new Base64();
String encryptedValue = Base64.encodeBytes(encVal);
return encryptedValue;
}

public static String decrypt(String encryptedData) throws Exception {
Key key = generateKey();
Cipher c = Cipher.getInstance(ALGO);
c.init(Cipher.DECRYPT_MODE, key);
byte[] decordedValue = Base64.decode(encryptedData);
byte[] decValue = c.doFinal(decordedValue);
String decryptedValue = new String(decValue);
return decryptedValue;
}

private static Key generateKey() throws Exception {
Key key = new SecretKeySpec(keyValue, ALGO);
return key;
}
public static void main(String[] args) throws Exception {

        String password = "346333";
        String passwordEnc = encrypt(password);
        String passwordDec = decrypt(passwordEnc);

        System.out.println("Plain Text : " + password);
        System.out.println("Encrypted Text : " + passwordEnc);
        System.out.println("Decrypted Text : " + passwordDec);
    }

}
