public static byte[] encrypt(byte[] value, String cryptoPass) {
    try {
        DESKeySpec keySpec = new DESKeySpec(cryptoPass.getBytes("UTF8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(keySpec);

        // Cipher is not thread safe
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        Log.d("bitmap", "Decrypted: " + value + " -> " + value);
        String encryptedValue = Base64.encodeToString(cipher.doFinal(value), Base64.DEFAULT);
        Log.d("bitmap", "Encrypted: " + value + " -> " + encryptedValue);

        return encryptedValue.getBytes("UTF-8");

    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (InvalidKeySpecException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    }
    return value;
};

public static byte[] decrypt(byte[] value, String cryptoPass) {

    try {
        DESKeySpec keySpec = new DESKeySpec(cryptoPass.getBytes("UTF8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(keySpec);

        byte[] encrypedPwdBytes = Base64.decode(value, Base64.DEFAULT);
        // cipher is not thread safe
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, key);

        return (cipher.doFinal(encrypedPwdBytes));

    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (InvalidKeySpecException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    }
    return value;
}
