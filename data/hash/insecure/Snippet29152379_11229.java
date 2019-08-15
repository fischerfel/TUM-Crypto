public static void main(String[] args) throws UnsupportedEncodingException, GeneralSecurityException {
    String ciphertext = "U2FsdGVkX1+0m/gle/XQX1shjnpveUrl1fO3oOlurPMlTks6+oQlEPfOrucihzEz";
    String plaintext = "This is some example plaintext";
    String password = "This is a very strong password";
    int keySize = 256;
    int ivSize = 128;

    // var wordArray = WordArray.create([0x53616c74, 0x65645f5f]).concat(salt).concat(ciphertext);
    byte[] ctBytes = Base64.getDecoder().decode(ciphertext.getBytes("UTF-8"));
    System.out.println("Is salted: " + Arrays.equals(Arrays.copyOf(ctBytes, 8), new byte[]{0x53, 0x61, 0x6c, 0x74, 0x65, 0x64, 0x5f, 0x5f}));
    System.out.println("Is salted: " + new String(Arrays.copyOf(ctBytes, 8)).equals("Salted__"));

    byte[] saltBytes = Arrays.copyOfRange(ctBytes, 8, 16);
    System.out.println("Salt matches: " + Arrays.equals(saltBytes, hexStringToByteArray("b49bf8257bf5d05f")));

    byte[] ciphertextBytes = Arrays.copyOfRange(ctBytes, 16, ctBytes.length);
    System.out.println("CT matches: " + Arrays.equals(ciphertextBytes, hexStringToByteArray("5b218e7a6f794ae5d5f3b7a0e96eacf3254e4b3afa842510f7ceaee722873133")));

    byte[] key = new byte[keySize/8];
    byte[] iv = new byte[ivSize/8];
    EvpKDF(password.getBytes("UTF-8"), keySize, ivSize, saltBytes, key, iv);

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
    byte[] recoveredPlaintextBytes = cipher.doFinal(ciphertextBytes);
    String recoveredPlaintext = new String(recoveredPlaintextBytes);

    System.out.println("Recovered Plaintext: " + recoveredPlaintext);
    System.out.println("Expected Plaintext: " + plaintext);
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
 * Copied from https://stackoverflow.com/a/140861
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
