static {
    try {
        Field field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
        field.setAccessible(true);
        field.set(null, java.lang.Boolean.FALSE);
    } catch (Exception ex) {
    }
}

public static void main(String[] args) throws UnsupportedEncodingException, GeneralSecurityException {
    String plaintext = "someplaintext";
    String password = "BtDMQ7RfNVoRzWGjS2DK";
    int keySize = 256;
    int ivSize = 128;

    byte[] salt = DatatypeConverter.parseHexBinary("5ba2b0e0bb968f47"); // yes, for testing, I use a fixed salt

    byte[] key = new byte[keySize/8];
    byte[] iv = new byte[ivSize/8];
    EvpKDF(password.getBytes("UTF-8"), keySize, ivSize, salt, key, iv);

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
    byte[] encryptedText = cipher.doFinal((plaintext).getBytes());

    System.out.println("SALT : " + DatatypeConverter.printHexBinary(salt));
    System.out.println("IV : " + DatatypeConverter.printHexBinary(iv));
    System.out.println("CT : " + DatatypeConverter.printBase64Binary(encryptedText));
// I sent salt, IV, and ciphertext to CryptoJS
}

public static byte[] EvpKDF(byte[] password, int keySize, int ivSize, byte[] salt, byte[] resultKey, byte[] resultIv) throws NoSuchAlgorithmException {
    return EvpKDF(password, keySize, ivSize, salt, 1, "MD5", resultKey, resultIv);
}

public static byte[] EvpKDF(byte[] password, int keySize, int ivSize, byte[] salt, int iterations, String hashAlgorithm, byte[] resultKey, byte[] resultIv) throws NoSuchAlgorithmException {
    keySize = keySize / 32;
    ivSize = ivSize / 32;
    int targetKeySize = keySize + ivSize;
    byte[] derivedBytes = new byte[targetKeySize * 4];
    int numberOfDerivedWords = 0;
    byte[] block = null;
    MessageDigest hasher = MessageDigest.getInstance(hashAlgorithm);
    while (numberOfDerivedWords < targetKeySize) {
        if (block != null) {
            hasher.update(block);
        }
        hasher.update(password);
        block = hasher.digest(salt);
        hasher.reset();

        // Iterations
        for (int i = 1; i < iterations; i++) {
            block = hasher.digest(block);
            hasher.reset();
        }

        System.arraycopy(block, 0, derivedBytes, numberOfDerivedWords * 4,
                Math.min(block.length, (targetKeySize - numberOfDerivedWords) * 4));

        numberOfDerivedWords += block.length/4;
    }

    System.arraycopy(derivedBytes, 0, resultKey, 0, keySize * 4);
    System.arraycopy(derivedBytes, keySize * 4, resultIv, 0, ivSize * 4);

    return derivedBytes; // key + iv
}

/**
 * Copied from http://stackoverflow.com/a/140861
 * */
public static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                + Character.digit(s.charAt(i+1), 16));
    }
    return data;
}
