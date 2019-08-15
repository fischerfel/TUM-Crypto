public static byte[] encryptFile(String pFilePath, byte[] pKey) throws GeneralSecurityException, IOException
{
    File file = new File(pFilePath);
    long length = file.length();
    InputStream is = new FileInputStream(file);

    // You cannot create an array using a long type.
    // It needs to be an int type.
    // Before converting to an int type, check
    // to ensure that file is not larger than Integer.MAX_VALUE.
    if (length > Integer.MAX_VALUE) {
        // File is too large
    }

    // Create the byte array to hold the data
    byte[] bytes = new byte[(int)length];

    // Read in the bytes
    int offset = 0;
    int numRead = 0;
    while (offset < bytes.length
           && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
        offset += numRead;
    }

    // Close the input stream and return bytes
    is.close();

    // Ensure all the bytes have been read in
    if (offset < bytes.length) {

        throw new IOException("Could not completely read file "+file.getName());
    }

    SecretKeyFactory lDESedeKeyFactory = SecretKeyFactory.getInstance("DESede");
    SecretKey kA = lDESedeKeyFactory.generateSecret(new DESedeKeySpec(pKey));

    IvParameterSpec lIVSpec = new IvParameterSpec(new byte[8]);
    Cipher desedeCBCCipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");

    desedeCBCCipher.init(Cipher.ENCRYPT_MODE, kA, lIVSpec);
    byte[] encrypted = desedeCBCCipher.doFinal(bytes);

    return encrypted;
}
