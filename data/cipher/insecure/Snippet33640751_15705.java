public class PasswordEncryption {

    public static final String AES = "AES";

    public static String encrypt(String value, File keyFile)
            throws GeneralSecurityException, IOException {
        if (!keyFile.exists()) {
            KeyGenerator keyGen = KeyGenerator
                    .getInstance(PasswordEncryption.AES);

            keyGen.init(128);
            SecretKey sk = keyGen.generateKey();
            FileWriter fw = new FileWriter(keyFile);
            fw.write(byteArrayToHexString(sk.getEncoded()));
            fw.flush();
            fw.close();
        }

        SecretKeySpec sks = getSecretKeySpec(keyFile);
        Cipher cipher = Cipher.getInstance(PasswordEncryption.AES);
        cipher.init(Cipher.ENCRYPT_MODE, sks, cipher.getParameters());
        byte[] encrypted = cipher.doFinal(value.getBytes());
        return byteArrayToHexString(encrypted);
    }

    public static String decrypt(String message, File keyFile)
            throws GeneralSecurityException, IOException {
        SecretKeySpec sks = getSecretKeySpec(keyFile);
        Cipher cipher = Cipher.getInstance(PasswordEncryption.AES);
        cipher.init(Cipher.DECRYPT_MODE, sks);
        byte[] decrypted = cipher.doFinal(hexStringToByteArray(message));
        return new String(decrypted);
    }

    private static SecretKeySpec getSecretKeySpec(File keyFile)
            throws NoSuchAlgorithmException, IOException {
        byte[] key = readKeyFile(keyFile);
        SecretKeySpec sks = new SecretKeySpec(key, PasswordEncryption.AES);
        return sks;
    }

    private static byte[] readKeyFile(File keyFile)
            throws FileNotFoundException {
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(keyFile).useDelimiter("\\Z");
        String keyValue = scanner.next();
        scanner.close();
        return hexStringToByteArray(keyValue);
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

    private static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    public static void main(String[] args) throws Exception {
        final String KEY_FILE = "/Users/xxx/key";
        final String PASSWORD_FILE = "/Users/xxx/properties";

        String openPwd = "my password is datasecurity";
        String openUser = "a user is ME";

        Properties p1 = new Properties();

        String encryptedPwd = PasswordEncryption.encrypt(openPwd, new File(
                KEY_FILE));
        String encryptedUser = PasswordEncryption.encrypt(openUser, new File(
                KEY_FILE));
        p1.put("password",encryptedPwd);
        p1.put("user",encryptedUser);
        p1.store(new FileWriter(PASSWORD_FILE),"");

        // ==================
        Properties p2 = new Properties();

        p2.load(new FileReader(PASSWORD_FILE));
        encryptedPwd = p2.getProperty("password");
        encryptedUser = p2.getProperty("user");
        System.out.println(encryptedPwd);
        System.out.println(encryptedUser);
        System.out.println(PasswordEncryption.decrypt(encryptedPwd, new File(
                KEY_FILE)));
        System.out.println(PasswordEncryption.decrypt(encryptedUser, new File(
                KEY_FILE)));
    }
}
