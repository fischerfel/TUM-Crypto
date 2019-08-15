private static final String ALGORITHM = "DESede/CBC/PKCS5Padding";


    void main() {

    MessageDigest md = MessageDigest.getInstance("md5");
    byte[] digestOfPassword = md.digest("O".getBytes("UTF-8"));
    byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
    byte[] ivBytes = Arrays.copyOf(digestOfPassword, 8);
    final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
    FileInputStream fis = new FileInputStream(new File("7za920.zip.enc"));
    FileOutputStream fos = new FileOutputStream(new File("7za920.zip"));
    decrypt(key, ivBytes, fis, fos);
    }


    private static void decrypt(SecretKey key, byte[] iv, InputStream is, OutputStream os) {
    try {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        CipherInputStream cis = new CipherInputStream(is, cipher);
        doCopy(cis, os);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void doCopy(InputStream is, OutputStream os) throws IOException {
    try {
        byte[] bytes = new byte[4096];
        int numBytes;
        while ((numBytes = is.read(bytes)) != -1) {
            os.write(bytes, 0, numBytes);
        }
        } finally {
            is.close();
            os.close();
        }
    }

    // only for demonstration
    private static byte[] encrypt(SecretKey key, IvParameterSpec iv, InputStream is, OutputStream os) {
    try {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        CipherInputStream cis = new CipherInputStream(is, cipher);
        doCopy(cis, os);
        return cipher.getIV();
    } catch (Exception ex) {
        throw new RuntimeException(ex);
    }
}
