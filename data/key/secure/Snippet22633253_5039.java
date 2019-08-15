public static byte[] encryptAESWithIV(byte[] key, byte[] clearText)
    throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, 
           InvalidParameterSpecException, IllegalBlockSizeException, 
           BadPaddingException, UnsupportedEncodingException {

    SecretKeySpec skeySpec = new SecretKeySpec(key, CIPHER_AES);
    Cipher cipher = Cipher.getInstance(CIPHER_AES_MODE);

    byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    //random.nextBytes(iv);

    try {
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(iv)); 
    } catch (InvalidAlgorithmParameterException e) {
        e.printStackTrace();
    }

    byte[] encrypted = cipher.doFinal(clearText);
    byte[] output = new byte[encrypted.length + iv.length];
    System.arraycopy(iv, 0, output, 0, iv.length);
    System.arraycopy(encrypted, 0, output, iv.length, encrypted.length);
    Log.v("encrypt-length",""+encrypted.length);
    Log.v("iv-length",""+iv.length);
    //Log.d("output_bytes", Arrays.toString(output));
    return output;  
}
