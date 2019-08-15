public class RSAEncryption {
public static final String KEYGENALGORITHM = "RSA";
public static final String ALGORITHM = "RSA/ECB/PKCS1Padding";

public static KeyPairContainer generateKey() {
      KeyPairGenerator keyGen;
      KeyPair key;
    try {
        keyGen = KeyPairGenerator.getInstance(KEYGENALGORITHM);
        keyGen.initialize(1024);
        key = keyGen.generateKeyPair();
        return new KeyPairContainer(key.getPublic(), key.getPrivate());
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
        System.out.println("Error: No such algorithm");
    }
    return null;
 }

public static String pubkeyToString(PublicKey key){
    byte[] array = key.getEncoded();
    BASE64Encoder encoder = new BASE64Encoder();
    String tempstring = encoder.encode(array);
    return tempstring;
}

public static PublicKey stringToPubKey(String string){
    BASE64Decoder decoder = new BASE64Decoder();
    try {
        byte[] array = decoder.decodeBuffer(string);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(array);
        KeyFactory keyFact = KeyFactory.getInstance(KEYGENALGORITHM);
        PublicKey pubKey = keyFact.generatePublic(x509KeySpec);
        return pubKey;
    } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } return null;
}

public static byte[] rSAencrypt(byte[] plaintext, String keystring) {
    Cipher cipher;
    try {
        PublicKey key = stringToPubKey(keystring);
        cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherText = cipher.doFinal(plaintext);
        //String cipherText = new String(cipherTextbytes);
        return cipherText;
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (BadPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return null;

}

public static byte[] rSAdecrypt(byte[] ciphertext, PrivateKey key){
    Cipher cipher;
    try {
        //byte[] ciphertext = ciphertextstring.getBytes();
        cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedText = cipher.doFinal(ciphertext);
        return decryptedText;
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (BadPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return null;
}
}
