public class TripleDES {

    private static sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
    private static sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();

    public static void main(String[] args) {

        try {
            try {
                Cipher c = Cipher.getInstance("DESede");
            } catch (Exception e) {
                System.err.println("Installing SunJCE provider.");
                Provider sunjce = new com.sun.crypto.provider.SunJCE();
                Security.addProvider(sunjce);
            }

            File keyFile = new File("D:/3DES/keygen.txt");

            /*
             * writeKey(generateKey(),keyFile);
             * System.out.println("After writing to file");
             */

            SecretKey rKey = readKey(keyFile);
            System.out.println("Secret key :" + rKey);
            String encryptedMssg = encrypt(rKey, "afda");
            System.out.println("Encrypted mssg :" + encryptedMssg);

            String decryptedMssg = decrypt(rKey, encryptedMssg);
            System.out.println("Decrypted mssg :" + decryptedMssg);

        } catch (Exception e) {
            System.err.println(e);
            System.err.println("Usage: java " + TripleDES.class.getName()
                    + " -d|-e|-g <keyfile>");
        }
    }

    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keygen = KeyGenerator.getInstance("DESede");
        System.out.println(keygen.generateKey());
        return keygen.generateKey();
    }

    public static void writeKey(SecretKey key, File f) throws IOException,
            NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
        DESedeKeySpec keyspec = (DESedeKeySpec) keyfactory.getKeySpec(key,
                DESedeKeySpec.class);
        byte[] rawkey = keyspec.getKey();
        FileOutputStream out = new FileOutputStream(f);
        out.write(rawkey);
        out.close();
    }

    public static SecretKey readKey(File f) throws IOException,
            NoSuchAlgorithmException, InvalidKeyException,
            InvalidKeySpecException {
        DataInputStream in = new DataInputStream(new FileInputStream(f));
        byte[] rawkey = new byte[(int) f.length()];
        in.readFully(rawkey);
        in.close();
        DESedeKeySpec keyspec = new DESedeKeySpec(rawkey);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
        SecretKey key = keyfactory.generateSecret(keyspec);

        return key;
    }

    public static SecretKey readKey(byte[] rawkey) throws IOException,
            NoSuchAlgorithmException, InvalidKeyException,
            InvalidKeySpecException {
        // DataInputStream in = new DataInputStream(new FileInputStream(f));
        // byte[] rawkey = new byte[(int)f.length()];
        // in.readFully(rawkey);
        // in.close();
        DESedeKeySpec keyspec = new DESedeKeySpec(rawkey);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
        SecretKey key = keyfactory.generateSecret(keyspec);

        return key;
    }

    public static String encrypt(SecretKey key, String clearData)
            throws NoSuchAlgorithmException, InvalidKeyException,
            NoSuchPaddingException, IOException, Exception {
        System.out.println("inside encrypt");
        System.out.println("KEYYYYY::" + key);
        Cipher cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cleartext = clearData.getBytes("ASCII");
        byte[] ciphertext = cipher.doFinal(cleartext);

        return encoder.encode(ciphertext);
    }

    public static String decrypt(SecretKey key, String encryptedData)
            throws NoSuchAlgorithmException, InvalidKeyException, IOException,
            IllegalBlockSizeException, NoSuchPaddingException,
            BadPaddingException, Exception {
        Cipher cipher = Cipher.getInstance("DESede");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] dec = decoder.decodeBuffer(encryptedData);
        byte[] clearbytes = cipher.doFinal(dec);

        return new String(clearbytes, "ASCII");
    }


}
