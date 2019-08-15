private final static String SECRETE_KEY = "xxxxxxxxxxxxxxxx";
private final static String IV = "0000000000000000";

/**
 * Encrypt the message with the secrete key in parameter
 */
public static String encrypt(String message) throws Exception {
    // The input length to encrypt should be a multiple of 16
    while (message.length() % 16 != 0) {
        message += " ";
    }

    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");

    SecretKeySpec key = new SecretKeySpec(SECRETE_KEY.getBytes("UTF-8"), "AES");

    cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));

    byte[] cipherByteArray = cipher.doFinal(message.getBytes("UTF-8"));

    String cipherMsg = "";
    for (int i = 0; i < cipherByteArray.length; i++) {
        cipherMsg += (char) cipherByteArray[i];
    }

    return cipherMsg;
}

/**
 * Decrypt the message with the secrete key in parameter
 */
public static String decrypt(String cipherMsg) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");

    SecretKeySpec key = new SecretKeySpec(SECRETE_KEY.getBytes("UTF-8"), "AES");

    cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));

    byte[] cipherByteArray = new byte[cipherMsg.length()];
    for (int i = 0; i < cipherMsg.length(); i++) {
        cipherByteArray[i] = (byte) cipherMsg.charAt(i);
    }

    return new String(cipher.doFinal(cipherByteArray));
}
