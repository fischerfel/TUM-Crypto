ppublic class EncryptionDecrption {

    private static final String ALGO = "AES";
    private static final byte[] keyValue = new byte[]{'T', 'h', 'e', 'R', 'o', 'o', 'K', 'n', 'a', 't','E','n', 'i', 'r','i','n'};

    public EncryptionDecrption(){

    }

    public static String setEncryptedString(String data) throws Exception {
        Key key = getKey();
        Cipher cipher = Cipher.getInstance(ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedValue = cipher.doFinal(data.getBytes("UTF-8"));

        return Base64.encodeToString(encryptedValue, Base64.DEFAULT);
    }

    public static String getDecryptedValue(String data) throws Exception {

        if(data != null) {
            Key key = getKey();
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodebyte = Base64.decode(data.getBytes("UTF-8"), Base64.DEFAULT);
            byte[] decValue = cipher.doFinal(decodebyte);

            return new String(decValue);
        }

        return null;
    }

    private static Key getKey() throws Exception {
        return new SecretKeySpec(keyValue, ALGO);
    }
}
