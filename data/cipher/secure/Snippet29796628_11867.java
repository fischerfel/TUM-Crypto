private static final String TAG = "AESCrypt";

// AESCrypt-ObjC uses CBC and PKCS7Padding
private static final String AES_MODE = "AES/CBC/PKCS7Padding";
private static final String CHARSET = "UTF-8";

// AESCrypt-ObjC uses SHA-256 (and so a 256-bit key)
private static final String HASH_ALGORITHM = "SHA-256";





    byte[] key = hexStringToByteArray("E0E1E2E3E5E6E7E8EAEBECEDEFF0F1F2");
    SecretKeySpec key2 = new SecretKeySpec(key, 0, key.length, "AES");
    byte[] ivBytes = hexStringToByteArray("12CEC438810CFA399A81139AF7D648BC");
    byte[] todecode = Base64.decode("CD46009A232420B2CBF6E4148EE17AA4",
            Base64.NO_WRAP);
    try {

        resultbytes = AESCrypt.decrypt(key2, ivBytes, todecode);
        result = resultbytes.toString();

    } catch (Exception e) {

        // TODO Auto-generated catch block
        feedbackBody.append("catch blok \n");
        e.printStackTrace();

    }
    feedbackBody.append(result + " \n");



public static byte[] decrypt(final SecretKeySpec key, final byte[] iv,
        final byte[] decodedCipherText) throws GeneralSecurityException {
    final Cipher cipher = Cipher.getInstance(AES_MODE);
    IvParameterSpec ivSpec = new IvParameterSpec(iv);
    cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
    byte[] decryptedBytes = cipher.doFinal(decodedCipherText);

    log("decryptedBytes", decryptedBytes);

    return decryptedBytes;
}
