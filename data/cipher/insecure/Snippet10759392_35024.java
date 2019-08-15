public class Test {

public String encryptionKey;

public static void main(String args[]) {
    Test t = new Test();
    String encrypt = t.encrypt("mypassword");
    System.out.println("decrypted value:" + t.decrypt(t.encryptionKey, encrypt));
}

public String encrypt(String value) {
    try {
        // Get the KeyGenerator
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(256);
        // Generate the secret key specs.
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        String key = new Base64().encodeAsString(raw);
        this.encryptionKey = key;
        System.out.println("------------------Key------------------");
        System.out.println(key);
        System.out.println("--------------End of Key---------------");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        String encrypt = (new Base64()).encodeAsString(cipher.doFinal(value.getBytes()));
        System.out.println("encrypted string:" + encrypt);
        return encrypt;
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalBlockSizeException ex) {
        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
    } catch (BadPaddingException ex) {
        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InvalidKeyException ex) {
        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchPaddingException ex) {
        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
}

public String decrypt(String key, String encrypted) {
    try {
        Key k = new SecretKeySpec(Base64.getDecoder().decode(key), "AES");
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, k);
        byte[] decodedValue = Base64.getDecoder().decode(encrypted);
        byte[] decValue = c.doFinal(decodedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    } catch (IllegalBlockSizeException ex) {
        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
    } catch (BadPaddingException ex) {
        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InvalidKeyException ex) {
        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchPaddingException ex) {
        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
}
