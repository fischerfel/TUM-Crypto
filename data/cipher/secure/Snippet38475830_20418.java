private byte[] decryptKey(byte[] encryptedKey, byte[] p, PrivateKey privateKey)
{
    Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING", "BC");
    PSource pSource = new PSource.PSpecified(p);

    cipher.init(Cipher.DECRYPT_MODE, privateKey,
        new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, pSource));

    return cipher.doFinal(encryptedKey);
}
