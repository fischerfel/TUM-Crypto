byte[] encodeData(byte[] key, byte[] data)
{
    try {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        return cipher.doFinal(data);
    } catch(Exception e) {
        Log.e("Cryptography", e.getMessage());
    }
    return new byte[0];
}
