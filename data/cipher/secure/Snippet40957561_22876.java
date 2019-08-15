byte[] secret = encrypt(publicKey, challenge.getBytes(StandardCharsets.UTF_8));
challengeenc = bytes2String(secret);

public byte[] encrypt(PublicKey key, byte[] plaintext) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
{
    Cipher enc = Cipher.getInstance("RSA");
    enc.init(Cipher.ENCRYPT_MODE, key);
    return enc.doFinal(plaintext);
}

private static String bytes2String(byte[] bytes)
{
    StringBuilder string = new StringBuilder();
    for (byte b : bytes) {
        String hexString = Integer.toHexString(0x00FF & b);
        string.append(hexString.length() == 1 ? "0" + hexString : hexString);
    }
    return string.toString();
}
