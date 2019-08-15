public class AESencrp {

    private static final String ALGO = "AES";
    private static byte[] keyValue = {};

    public static String encrypt(String Data, Key key) throws Exception {
        System.out.println("Key Encryp ============="
                + new String(key.getEncoded()));
        System.out.println("\n\n key in Enc ==========>" + key.getEncoded());
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        byte encryptedValue[] = new Base64().encode(encVal);
        return new String(encryptedValue);
    }

    public static String decrypt(String encryptedData, Key key)
            throws Exception {
        System.out.println("Key Decryp ============="
                + new String(key.getEncoded()));
        System.out.println("\n\n key in Dec ==========>" + key.getEncoded());
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new Base64().decode(encryptedData.getBytes());
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    public static void main(String[] args) throws Exception {

        try {
            String sPrivateKey = "privateKey";
            keyValue = sPrivateKey.getBytes();
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            keyValue = sha.digest(keyValue);
            keyValue = Arrays.copyOf(keyValue, 16); // use only first 128 bit
        } catch (Exception e) {
            e.printStackTrace();
        }

        Key key = new SecretKeySpec(keyValue, ALGO);
        System.out.println("\n\n key in GK ==========>" + key.getEncoded());
        System.out.println("\n\n key in GK ==========>"
                + new String(key.getEncoded()));
        String password = "mypassword";
        String passwordEnc = encrypt(password, key);
        String passwordDec = decrypt(passwordEnc, key);

        System.out.println("Plain Text : " + password);
        System.out.println("Encrypted Text : " + passwordEnc);
        System.out.println("Decrypted Text : " + passwordDec);
    }

}
