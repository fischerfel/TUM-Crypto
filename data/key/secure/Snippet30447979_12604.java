private byte[] K;
public void setK(){
    KeyGenerator KeyGen=KeyGenerator.getInstance("AES");
    KeyGen.init(128);
    SecretKey key=KeyGen.generateKey();
    K = key.getEncoded();
}
public String encrypt(byte[] input){
    try {
        IvParameterSpec iv = new IvParameterSpec(Base64.decode("Hola".getBytes(), Base64.DEFAULT));
        SecretKeySpec key = new SecretKeySpec(K, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
        int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
        ctLength += cipher.doFinal(cipherText, ctLength);
        return Base64.encodeToString(cipherText, Base64.DEFAULT);
    } catch (Exception e) {
        Log.e(JUAN, "failed to encrypt ", e);
    }
    return null;
}

public String decrypt(byte[] input){
    try {
        IvParameterSpec iv = new IvParameterSpec(Base64.decode("Hola".getBytes(), Base64.DEFAULT));
        SecretKeySpec key = new SecretKeySpec(K, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] plainText = new byte[cipher.getOutputSize(input.length)];
        int ctLength = cipher.update(input, 0, input.length, plainText, 0);
        ctLength += cipher.doFinal(plainText, ctLength);
        return Base64.encodeToString(plainText, Base64.DEFAULT);
    } catch (Exception e) {
        Log.e(JUAN, "failed to decrypt ", e);
    }
    return null;
}
