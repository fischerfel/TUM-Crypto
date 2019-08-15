public static String decompress(String zipText) throws IOException {
    byte[] compressed = Base64.decode(zipText);
    if (compressed.length > 4) {
        GZIPInputStream gzipInputStream = new GZIPInputStream(
                new ByteArrayInputStream(compressed, 4,
                        compressed.length - 4));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int value = 0; value != -1;) {
            value = gzipInputStream.read();
            if (value != -1) {
                baos.write(value);
            }
        }
        gzipInputStream.close();
        baos.close();

        byte[] byteArray = baos.toByteArray();

        Log.i("toByteArray", String.valueOf(byteArray.length));

        String sReturn = new String(byteArray, "UTF-8");

        return sReturn;
    } else {
        return "";
    }
}



public static String decrypt(String encrypted, String password)
        throws Exception {

    byte[] encrypteddata = Base64.decode(encrypted);

    byte[] bytes = decrypt(encrypteddata, password);

    String result = new String(bytes, "UTF-8");

    return result;
}

public static byte[] decrypt(byte[] encrypted, String password)
        throws Exception {

    byte[] passwordKey = encodeDigest(password);
    try {
        aesCipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
    } catch (NoSuchAlgorithmException e) {
        throw new Exception(
                "Decryption Exception: No such algorithm\r\n" + e
                        .toString());
    } catch (NoSuchPaddingException e) {
        throw new Exception(
                "Decryption Exception: No such padding PKCS5\r\n" + e
                        .toString());
    }
    secretKey = new SecretKeySpec(passwordKey, CIPHER_ALGORITHM);

    try {
        aesCipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
    } catch (InvalidKeyException e) {
        throw new Exception(
                "Decryption Exception: Invalid key\r\n" + e.toString());
    } catch (InvalidAlgorithmParameterException e) {
        throw new Exception(
                "Decryption Exception: Invalid algorithm\r\n" + e
                        .toString());
    }

    byte[] encryptedData;
    try {
        encryptedData = aesCipher.doFinal(encrypted);
    } catch (IllegalBlockSizeException e) {
        throw new Exception(
                "Decryption Exception: Illegal block size\r\n" + e
                        .toString());
    } catch (BadPaddingException e) {
        throw new Exception(
                "Decryption Exception: Bad padding\r\n" + e
                        .toString());
    }
    return encryptedData;
}
