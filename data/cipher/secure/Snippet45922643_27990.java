public static String decrypt2(final String EncryptedMessageBase64,
                              final String symKeyHex,
                              final String sIvHex) {

    final byte[] symKeyData = Base64.decode((symKeyHex),Base64.DEFAULT);
    final byte[] byIvData = Base64.decode((sIvHex), Base64.DEFAULT);
    final byte[] EncryptedMessage = Base64.decode(EncryptedMessageBase64, Base64.DEFAULT);

    try
    {

        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        final int blockSize = cipher.getBlockSize();

        final SecretKeySpec symKey = new SecretKeySpec(symKeyData, "AES");
        Log.i("### iv size -------->", String.valueOf(blockSize));
        Log.i("### symKeyHex -------->", symKeyHex);
        Log.i("### sIvHex -------->", sIvHex);
        Log.i("### blockSize -------->", String.valueOf(blockSize));

        final IvParameterSpec iv = new IvParameterSpec(byIvData);

        final byte[] encryptedMessage = new byte[EncryptedMessage.length];

        cipher.init(Cipher.DECRYPT_MODE, symKey, iv);
