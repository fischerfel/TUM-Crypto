public static byte[] encryptByPublicKey(byte[] data, String key)
        throws Exception {

    key = key.replace("-----BEGIN RSA PUBLIC KEY-----\r\n", "").replace("-----END RSA PUBLIC KEY-----", "");

    byte[] bytes = decryptBASE64(key);
    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(bytes);

    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    PublicKey pkPublic = keyFactory.generatePublic(x509KeySpec);

    Cipher pkCipher = Cipher.getInstance("RSA");
    pkCipher.init(Cipher.ENCRYPT_MODE, pkPublic);

    return pkCipher.doFinal(data);
}
