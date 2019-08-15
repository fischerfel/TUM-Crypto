private static final String ALGORITHM = "AES";
    private static final int ITERATIONS = 5;
    private static final byte[] keyValue = new byte[] { '1', '2', '3', '4',
            '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6' };


    public static String encryptMessage(String message, String salt) {
        String encMessage = null;
        byte[] encVal = null;
        String messageWithSalt = null;
        try {
            Key key = generateKey();

            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, key);

            for (int i = 0; i < ITERATIONS; i++) {
                messageWithSalt = salt + encMessage;
                encVal = c.doFinal(messageWithSalt.getBytes());
                byte[] encryptedValue = new Base64().encode(encVal);
                encMessage = new String(encryptedValue);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return encMessage;
    }
