public static byte[] encrypt(String passphrase, byte[] data) throws Exception {

    // Hash the ASCII-encoded passphrase with md5

    byte[] keyData = passphrase.getBytes(Charset.forName("US-ASCII"));
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte [] md5HashOfKey = md.digest(keyData);

    // Need to use bouncycastle (spongycastle on Android) to get RC2

    Security.addProvider(new BouncyCastleProvider());

    Cipher rc2 = Cipher.getInstance("RC2/CBC/PKCS5PADDING");

    // Create an RC2 40-bit key from the 1st 5 bytes of the hash.

    SecretKeySpec rc2KeySpec = new SecretKeySpec(md5HashOfKey, 0, 5, "RC2");
    rc2.init(Cipher.ENCRYPT_MODE, rc2KeySpec);

    byte [] cipher = rc2.doFinal(data);

    return cipher;
}
