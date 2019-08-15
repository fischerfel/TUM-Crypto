private final static String ALGORITM = "Blowfish";
private final static String KEY = "256342ba5781f80a72dad3f90aeee8ba93c7637aaf218a8b8c18c";
private final static String PLAIN_TEXT = "here is your text";

public void run(View v) {

    try {

        byte[] encrypted = encrypt(KEY, PLAIN_TEXT);
        Log.i("FOO", "Encrypted: " + bytesToHex(encrypted));

        String decrypted = decrypt(KEY, encrypted);
        Log.i("FOO", "Decrypted: " + decrypted);

    } catch (GeneralSecurityException e) {
        e.printStackTrace();
    }
}

private byte[] encrypt(String key, String plainText) throws GeneralSecurityException {

    SecretKey secret_key = new SecretKeySpec(key.getBytes(), ALGORITM);

    Cipher cipher = Cipher.getInstance(ALGORITM);
    cipher.init(Cipher.ENCRYPT_MODE, secret_key);

    return cipher.doFinal(plainText.getBytes());
}

private String decrypt(String key, byte[] encryptedText) throws GeneralSecurityException {

    SecretKey secret_key = new SecretKeySpec(key.getBytes(), ALGORITM);

    Cipher cipher = Cipher.getInstance(ALGORITM);
    cipher.init(Cipher.DECRYPT_MODE, secret_key);

    byte[] decrypted = cipher.doFinal(encryptedText);

    return new String(decrypted);
}

public static String bytesToHex(byte[] data) {

    if (data == null)
        return null;

    String str = "";

    for (int i = 0; i < data.length; i++) {
        if ((data[i] & 0xFF) < 16)
            str = str + "0" + java.lang.Integer.toHexString(data[i] & 0xFF);
        else
            str = str + java.lang.Integer.toHexString(data[i] & 0xFF);
    }

    return str;

}
