public class AESencrp {
private static final String ALGO = "AES";
private static final byte[] keyValue = new byte[] { 'S', 'D', 'P', 'i', 'b', 'm', 'B','H', 'A', 'R', 'T','I', 'P', 'K', 'e', 'y' };

public static String encrypt(String Data) throws Exception {
    System.out.println(".............Encryption start............");
    Key key = generateKey();
    System.out.println("Key : " + key);
    Cipher c = Cipher.getInstance(ALGO);
    c.init(Cipher.ENCRYPT_MODE, key);
    byte[] encVal = c.doFinal(Data.getBytes());
    //System.out.println("encVal in byte[] : " + encVal);
    String encryptedValue = new BASE64Encoder().encode(encVal);
    System.out.println("encryptedValue byusing base64 : " + encryptedValue);
    System.out.println("..............Encryption End............");
    return encryptedValue;
}

public static String decrypt(String encryptedData) throws Exception {
    //final byte[] keyValue1 =  new byte[] { 'T', 'h', 'e', 'B', 'e', 's', 't','S', 'e', 'c', 'r','e', 't', 'K', 'e', 'y' };
    System.out.println("");
    System.out.println("");
    System.out.println(".............Decryption start............");
    Key key = generateKey();
    //Key key = new SecretKeySpec(keyValue1, ALGO);
    //System.out.println("Key : " + key);
    Cipher c = Cipher.getInstance(ALGO);
    c.init(Cipher.DECRYPT_MODE, key);
    byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
    //System.out.println("decryptedValue byusing base64 : " + decordedValue);
    byte[] decValue = c.doFinal(decordedValue);
   // System.out.println("decValue in byte[] : " + decValue);
    String decryptedValue = new String(decValue);
    System.out.println("String representation of decrypted value: " + decryptedValue);
    System.out.println(".............Decryption End............");
    return decryptedValue;
}
private static Key generateKey() throws Exception {
    Key key = new SecretKeySpec(keyValue, ALGO);
    System.out.println("key is " + keyValue  );
    return key;
}


public static void main(String[] args) throws Exception {
    String password = "encrypt_this";
    String passwordEnc = AESencrp.encrypt(password);
    String passwordDec = AESencrp.decrypt(passwordEnc);
    System.out.println("");System.out.println("");
    System.out.println("Plain Text : " + password);
    System.out.println("Encrypted Text : " + passwordEnc);
    System.out.println("Decrypted Text : " + passwordDec);
   }
}
