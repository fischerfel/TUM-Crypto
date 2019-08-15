public static void encryptFile(String inFileName, String outFileName, char[] pass) throws IOException, GeneralSecurityException {
    Cipher cipher = PasswordProtectFile.makeCipher(pass, true);
    try (CipherOutputStream cipherOutputStream = new CipherOutputStream(new FileOutputStream(outFileName), cipher);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inFileName))) {
        int i;
        while ((i = bis.read()) != -1) {
            cipherOutputStream.write(i);
        }
    }
}

public static void decryptFile(String inFileName, String outFileName, char[] pass) throws GeneralSecurityException, IOException {
    Cipher cipher = PasswordProtectFile.makeCipher(pass, false);
    try (CipherInputStream cipherInputStream = new CipherInputStream(new FileInputStream(inFileName), cipher);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFileName))) {
        int i;
        while ((i = cipherInputStream.read()) != -1) {
            bos.write(i);
        }
    }
}

private static Cipher makeCipher(char[] pass, Boolean decryptMode) throws GeneralSecurityException {

    // Use a KeyFactory to derive the corresponding key from the passphrase:
    PBEKeySpec keySpec = new PBEKeySpec(pass);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
    SecretKey key = keyFactory.generateSecret(keySpec);

    // Create parameters from the salt and an arbitrary number of iterations:
    PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 43);

    // Set up the cipher:
    Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");

    // Set the cipher mode to decryption or encryption:
    if (decryptMode) {
        cipher.init(Cipher.ENCRYPT_MODE, key, pbeParamSpec);
    } else {
        cipher.init(Cipher.DECRYPT_MODE, key, pbeParamSpec);
    }

    return cipher;
}
