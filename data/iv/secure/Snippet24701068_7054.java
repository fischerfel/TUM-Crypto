    public static String encryptMessage(final String plainMessage, final String symKeyHex) 
{
    final byte[] symKeyData = DatatypeConverter.parseHexBinary(symKeyHex);
    final byte[] encodedMessage = plainMessage.getBytes(Charset.forName("UTF-8"));

    try {

        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        final int blockSize = cipher.getBlockSize();

        // create the key
        final SecretKeySpec symKey = new SecretKeySpec(symKeyData, "AES");

        // generate random IV using block size (possibly create a method for
        // this)
        final byte[] ivData = new byte[blockSize];
        final SecureRandom rnd = SecureRandom.getInstance("SHA1PRNG");
        rnd.nextBytes(ivData);
        final IvParameterSpec iv = new IvParameterSpec(ivData);

        cipher.init(Cipher.ENCRYPT_MODE, symKey, iv);

        final byte[] encryptedMessage = cipher.doFinal(encodedMessage);

        // concatenate IV and encrypted message
        final byte[] ivAndEncryptedMessage = new byte[ivData.length + encryptedMessage.length];
        System.arraycopy(ivData, 0, ivAndEncryptedMessage, 0, blockSize);
        System.arraycopy(encryptedMessage, 0, ivAndEncryptedMessage, blockSize, encryptedMessage.length);

        final String ivAndEncryptedMessageBase64 = DatatypeConverter.printBase64Binary(ivAndEncryptedMessage);
        return ivAndEncryptedMessageBase64;


    }catch (InvalidKeyException e) 
    {
        throw new IllegalArgumentException("key argument does not contain a valid AES key");

    }catch (GeneralSecurityException e) 
    {
        throw new IllegalStateException("Unexpected exception during encryption", e);
    }
}
