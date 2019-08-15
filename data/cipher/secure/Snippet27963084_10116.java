public String decrypt(byte[] encrypted) throws Exception
{
    Cipher cipher = Cipher.getInstance("RSA");

    KeyFactory kf = KeyFactory.getInstance("RSA");

    byte[] encKey = readFromFile(PUBLIC_KEY_FILE, false);

    X509EncodedKeySpec ks = new X509EncodedKeySpec(encKey);

    PublicKey pk = kf.generatePublic(ks);

    cipher.init(Cipher.DECRYPT_MODE, pk);

    byte[] plainText = cipher.doFinal(encrypted);

    return new String(plainText,"UTF-8");
}
