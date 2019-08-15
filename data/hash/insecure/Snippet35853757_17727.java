public class Password1 {

    private static final String ALGO = "AES";
    private static byte[] keyValue = new byte[]{'t','h','y','u','e','f','z','s','y','k','f','l','d','a','b','m'};

    public static void main(String[] args) {
        //Password1 p = new Password1();
        Scanner sc = new Scanner(System.in);
        String i = sc.nextLine();
        System.out.println("Password = "+i);

        try {
            String en = encrypt(i);
            System.out.println(en);
            String dec = decrypt(en);

            System.out.println("Encrypted = " + en);
            System.out.println("Decrypted = " + dec);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String encrypt(String Data) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes("UTF-8"));
        String encrypted = new BASE64Encoder().encode(encVal);

        return encrypted;
    }

    public static String decrypt(String encrypted) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        //Byte bencrypted = Byte.valueOf(encrypted);
        byte[] decoded = new BASE64Decoder().decodeBuffer(encrypted);

        byte[] decValue = c.doFinal(decoded);
        String decrypted = new String(decValue);
        return decrypted;
    }

    private static Key generateKey() throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        keyValue = sha.digest(keyValue);
        keyValue = Arrays.copyOf(keyValue, 16);
        SecretKeySpec key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }

}
