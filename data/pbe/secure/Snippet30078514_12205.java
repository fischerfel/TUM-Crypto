public DesEncrypter(String passPhrase) {
    try {
        // Create the key
        KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt,
                iterationCount);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
                .generateSecret(keySpec);
        ecipher = Cipher.getInstance(key.getAlgorithm());
        dcipher = Cipher.getInstance(key.getAlgorithm());

        // Prepare the parameter to the ciphers
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt,
                iterationCount);

        // Create the ciphers
        ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
    } catch (java.security.InvalidAlgorithmParameterException e) {
    } catch (java.security.spec.InvalidKeySpecException e) {
    } catch (javax.crypto.NoSuchPaddingException e) {
    } catch (java.security.NoSuchAlgorithmException e) {
    } catch (java.security.InvalidKeyException e) {
    }
}

public String encrypt(String str) {
    try {
        // Encode the string into bytes using utf-8
        byte[] utf8 = str.getBytes("UTF8");

        // Encrypt
        byte[] enc = ecipher.doFinal(utf8);

        // Encode bytes to base64 to get a string
        return new sun.misc.BASE64Encoder().encode(enc);
    } catch (javax.crypto.BadPaddingException e) {
    } catch (IllegalBlockSizeException e) {
    } catch (UnsupportedEncodingException e) {
    }
    return null;
}

public String decrypt(String str) {
    try {
        // Decode base64 to get bytes
        byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

        // Decrypt
        byte[] utf8 = dcipher.doFinal(dec);

        // Decode using utf-8
        return new String(utf8, "UTF8");
    } catch( Exception ex) {
        ex.printStackTrace(System.err);
    }
    return null;
}
