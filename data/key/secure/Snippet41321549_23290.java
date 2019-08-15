public void decrypt(byte[] nkb, String crKey){
    //nkb is byte array formed by Base64 decoding of 'data' variable in the Javascript code
    //crKey corresponds to the 'masterkey' variable

    byte[] salt = Arrays.copyOfRange(nkb, 0, 64);
    byte[] iv = Arrays.copyOfRange(nkb, 64, 76);
    byte[] tag = Arrays.copyOfRange(nkb, 76, 92);
    byte[] text = Arrays.copyOfRange(nkb, 92, nkb.length);

    PBEKeySpec ks = new PBEKeySpec(crKey.toCharArray(), salt, iterations, 256);
    SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
    SecretKey pbeKey = skf.generateSecret(ks);

    byte[] decrypted = decrypt(iv, pbeKey.getEncoded(), text, tag);
}
    public static byte[] decrypt(byte[] ivBytes, byte[] keyBytes, byte[] textBytes, byte[] tagBytes)
        throws java.io.UnsupportedEncodingException,
        NoSuchAlgorithmException,
        NoSuchPaddingException,
        InvalidKeyException,
        InvalidAlgorithmParameterException,
        IllegalBlockSizeException,
        BadPaddingException,
        NoSuchProviderException {

        GCMParameterSpec ivSpec = new GCMParameterSpec(tagBytes.length*Byte.SIZE, ivBytes);

        SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
        return cipher.doFinal(textBytes); //getting tag mismatch error here
    }
