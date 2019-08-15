private byte[] simpleEncrypt(String value, String key)
{
    MessageDigest digest = null;
    byte[] hash = null;
    byte[] IV = null;
    try
    {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

        digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        hash = digest.digest(key.getBytes("UTF-8"));
        IV = Arrays.copyOfRange(hash, 0, cipher.getBlockSize());
        SecretKey secret = new SecretKeySpec(hash, "AES");
        IvParameterSpec ivspec = new IvParameterSpec(IV);
        cipher.init(Cipher.ENCRYPT_MODE, secret, ivspec);
        return cipher.doFinal(value.getBytes("UTF-8"));
    }
    catch (Exception e1)
    {
        statusBar.setVisibility(View.VISIBLE);
        progress.setVisibility(View.INVISIBLE);
        status.setText(e1.getLocalizedMessage());
    }
    return null;
}
