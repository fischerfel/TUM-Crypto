public class Cryptor {
private static Cryptor _instance = null;
private static Object mutex = new Object();
String symKeyString;
SecretKey symKey;
String keyString;
Cipher cipher;
String plainText;
String cipherText;
byte[] plainByte;
SecretKey originalKey;
SecretKey key;
KeyGenerator keyGen;
byte[] cipherByte;

private Cryptor() {
    try {
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public static Cryptor getInstance() {
    if (_instance == null) {
        synchronized (mutex) {
            if (_instance == null)
                _instance = new Cryptor();
        }
    }
    return _instance;

}

public String generateSymmetricKey() {

    try {
        keyGen = KeyGenerator.getInstance("AES");
        symKey = keyGen.generateKey();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }

    symKeyString = convertKeyToString(symKey);
    return symKeyString;
}


public String symEncrypt(String keyStr, String plainTextInput) {

    key = convertStringToKey(keyStr);

    try {
        cipher.init(Cipher.ENCRYPT_MODE, key);


        cipherByte = cipher.doFinal(plainTextInput.getBytes());

    } catch (Exception e) {
        e.printStackTrace();
    }
    cipherText = Base64.encodeToString(cipherByte, Base64.DEFAULT);
    return cipherText;
}


public String symDecrypt(String keyStr, String cipherText) {
    key = convertStringToKey(keyStr);

    try {
        cipher.init(Cipher.DECRYPT_MODE, key);

        plainByte = cipher.doFinal(cipherText.getBytes()); //here is where the exception is thrown!

    } catch (Exception e) {
        e.printStackTrace();
    }

    plainText = Base64.encodeToString(plainByte, Base64.DEFAULT);
    return plainText;
}


private String convertKeyToString(SecretKey key) {


    keyString = Base64.encodeToString(key.getEncoded(), Base64.DEFAULT);

    return keyString;
}

private SecretKey convertStringToKey(String keyStr) {
    byte[] decodedKey = Base64.decode(keyStr, Base64.DEFAULT);
    originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    return originalKey;
}
}
