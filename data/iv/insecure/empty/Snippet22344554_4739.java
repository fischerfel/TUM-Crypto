String dencryptReq = Utils.decrypt2(new String(Base64.decodeBase64(secretKeyInformation.getSecretKey().getBytes())),Base64.decodeBase64(encryptReq.getBytes()) );


public static String decrypt2(String key, byte[] encrypted)
        throws GeneralSecurityException {

    byte[] raw = Base64.decodeBase64(key.getBytes());
    if (raw.length != 16) {
        throw new IllegalArgumentException("Invalid key size.");
    }
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec,
            new IvParameterSpec(new byte[16]));
    byte[] original = cipher.doFinal(encrypted);

    return new String(original, Charset.forName("US-ASCII"));
}

But it is throwing me invalid key size exception.

If i do in one time this without saving in databse and fetching from database it is working fine.
