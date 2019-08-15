public static SecretKey generateKey() throws NoSuchAlgorithmException {
    // Generate a 256-bit key
    final int outputKeyLength = 256;
    SecureRandom secureRandom = new SecureRandom();
    // Do *not* seed secureRandom! Automatically seeded from system entropy.
    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    keyGenerator.init(outputKeyLength, secureRandom);
    yourKey = keyGenerator.generateKey();
    return yourKey;
}

public static byte[] encodeFile(SecretKey yourKey, byte[] fileData)
        throws Exception {
    byte[] encrypted = null;
    byte[] data = yourKey.getEncoded();
    SecretKeySpec skeySpec = new SecretKeySpec(data, 0, data.length,
            algorithm);
    Cipher cipher = Cipher.getInstance(algorithm);
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(
            new byte[cipher.getBlockSize()]));
    encrypted = cipher.doFinal(fileData);
    return encrypted;
}

public static byte[] decodeFile(SecretKey yourKey, byte[] fileData)
        throws Exception {
    byte[] decrypted = null;
    Cipher cipher = Cipher.getInstance(algorithm);
    cipher.init(Cipher.DECRYPT_MODE, yourKey, new IvParameterSpec(
            new byte[cipher.getBlockSize()]));
    decrypted = cipher.doFinal(fileData);
    return decrypted;
}

void saveFile(String stringToSave) {
    try {
        File file = new File(Environment.getExternalStorageDirectory()
                + File.separator, encryptedFileName);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(file));
        yourKey = generateKey();
        byte[] filesBytes = encodeFile(yourKey, stringToSave.getBytes());
        bos.write(filesBytes);
        bos.flush();
        bos.close();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

void decodeFile() {

    try {
        byte[] decodedData = decodeFile(yourKey, readFile());
        String str = new String(decodedData);
        System.out.println("DECODED FILE CONTENTS : " + str);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public byte[] readFile() {
    byte[] contents = null;

    File file = new File(Environment.getExternalStorageDirectory()
            + File.separator, encryptedFileName);
    int size = (int) file.length();
    contents = new byte[size];
    try {
        BufferedInputStream buf = new BufferedInputStream(
                new FileInputStream(file));
        try {
            buf.read(contents);
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
    return contents;
}
