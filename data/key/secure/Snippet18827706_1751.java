public static String AESDecrypt(String b64data, byte[] key, byte[] iv) throws CipherException {
    try {
        aesCipher_ = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesCipher_.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));

        final byte[] byteData = Base64.decode(b64data, Base64.DEFAULT);
        final byte[] decryptedData = aesCipher_.doFinal(byteData);

        return new String(decryptedData);
    } catch (Exception e) {
        throw new CipherException(e);
    }
}
