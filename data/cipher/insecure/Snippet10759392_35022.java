public static void main(String args[]) {
    Test t = new Test();
    String encrypt = new String(t.encrypt("mypassword"));
    System.out.println("decrypted value:" + t.decrypt("ThisIsASecretKey", encrypt));
}

public String encrypt(String value) {
    try {
        byte[] raw = new byte[]{'T', 'h', 'i', 's', 'I', 's', 'A', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(value.getBytes());
        System.out.println("encrypted string:" + (new String(encrypted)));
        return new String(skeySpec.getEncoded());
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
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(skeySpec.getEncoded(), "AES"));
        //getting error here
        byte[] original = cipher.doFinal(encrypted.getBytes());
        return new String(original);
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
