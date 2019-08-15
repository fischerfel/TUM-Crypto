public class StringEncrypter {
Cipher ecipher;
Cipher dcipher;


public StringEncrypter(SecretKey key, String algorithm) {
    try {
        ecipher = Cipher.getInstance(algorithm);
        dcipher = Cipher.getInstance(algorithm);
        ecipher.init(Cipher.ENCRYPT_MODE, key);
        dcipher.init(Cipher.DECRYPT_MODE, key);
    } catch (NoSuchPaddingException e) {
        System.out.println("EXCEPTION: NoSuchPaddingException");
    } catch (NoSuchAlgorithmException e) {
        System.out.println("EXCEPTION: NoSuchAlgorithmException");
    } catch (InvalidKeyException e) {
        System.out.println("EXCEPTION: InvalidKeyException");
    }
}



public StringEncrypter(String passPhrase) {

    setPassPhrase( passPhrase );

}


public void setPassPhrase( String passPhrase ) {

    // 8-bytes Salt
    byte[] salt = {
        (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
        (byte)0x56, (byte)0x34, (byte)0xE3, (byte)0x03
    };

    // Iteration count
    int iterationCount = 19;

    try {

        KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

        ecipher = Cipher.getInstance(key.getAlgorithm());
        dcipher = Cipher.getInstance(key.getAlgorithm());

        // Prepare the parameters to the cipthers
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

        ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

    } catch (InvalidAlgorithmParameterException e) {
        System.out.println("EXCEPTION: InvalidAlgorithmParameterException");
    } catch (InvalidKeySpecException e) {
        System.out.println("EXCEPTION: InvalidKeySpecException");
    } catch (NoSuchPaddingException e) {
        System.out.println("EXCEPTION: NoSuchPaddingException");
    } catch (NoSuchAlgorithmException e) {
        System.out.println("EXCEPTION: NoSuchAlgorithmException");
    } catch (InvalidKeyException e) {
        System.out.println("EXCEPTION: InvalidKeyException");
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

    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return null;
}



public String decrypt(String str) {

    try {

        // Decode base64 to get bytes
        byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
        System.out.println( "[decrypt]BASE64Decoded????? " + dec );
        System.out.println( "[decrypt]Algo: " + dcipher.getAlgorithm() );
        System.out.println( "[decrypt]Block Size: " + dcipher.getBlockSize() );
        System.out.println( "[decrypt]Parameters: " + dcipher.getParameters().getEncoded() );

        // Decrypt
        byte[] utf8 = dcipher.doFinal(dec);

        // Decode using utf-8
        return new String(utf8, "UTF8");

    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return null;
}
