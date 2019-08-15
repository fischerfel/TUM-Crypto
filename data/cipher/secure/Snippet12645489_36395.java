public class AESFiles {

    private byte[] getKeyBytes(final byte[] key) throws Exception {
        byte[] keyBytes = new byte[16];
        System.arraycopy(key, 0, keyBytes, 0, Math.min(key.length, keyBytes.length));
        return keyBytes;
    }

    public Cipher getCipherEncrypt(final byte[] key) throws Exception {
        byte[] keyBytes = getKeyBytes(key);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        return cipher;
    }

    public Cipher getCipherDecrypt(byte[] key) throws Exception {
        byte[] keyBytes = getKeyBytes(key);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        return cipher;
    }

    public void encrypt(File inputFile, File outputFile, byte[] key) throws Exception {
        Cipher cipher = getCipherEncrypt(key);
        FileOutputStream fos = null;
        CipherOutputStream cos = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(inputFile);
            fos = new FileOutputStream(outputFile);
            cos = new CipherOutputStream(fos, cipher);
            byte[] data = new byte[1024];
            int read = fis.read(data);
            while (read != -1) {
                cos.write(data, 0, read);
                read = fis.read(data);
                System.out.println(new String(data, "UTF-8").trim());
            }
            cos.flush();
        } finally {
            fos.close();
            cos.close();
            fis.close();
        }
    }

    public void decrypt(File inputFile, File outputFile, byte[] key) throws Exception {
        Cipher cipher = getCipherDecrypt(key);
        FileOutputStream fos = null;
        CipherInputStream cis = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(inputFile);
            cis = new CipherInputStream(fis, cipher);
            fos = new FileOutputStream(outputFile);
            byte[] data = new byte[1024];
            int read = cis.read(data);
            while (read != -1) {
                fos.write(data, 0, read);
                read = cis.read(data);
                System.out.println(new String(data, "UTF-8").trim());
            }
        } finally {
            fos.close();
            cis.close();
            fis.close();
        }
    }

    public static void main(String args[]) throws Exception {
        byte[] key = "mykey".getBytes("UTF-8");
        new AESFiles().encrypt(new File("C:\\Tests\\secure.txt"), new File("C:\\Tests\\secure.txt.aes"), key);
        new AESFiles().decrypt(new File("C:\\Tests\\secure.txt.aes"), new File("C:\\Tests\\secure.txt.1"), key);
    }
}
