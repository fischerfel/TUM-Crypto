public class ZipTest {

    public static void main(String [] args) throws Exception {
        String password = "password";
        write(password);
        read(password);
    }

    private static void write(String password) throws Exception {
        OutputStream target = new FileOutputStream("out.zip");
        target = new CipherOutputStream(target, createCipher(Cipher.ENCRYPT_MODE, password));
        ZipOutputStream output = new ZipOutputStream(target);

        ZipEntry e = new ZipEntry("filename");
        output.putNextEntry(e);
        output.write("helloWorld".getBytes());
        output.closeEntry();

        e = new ZipEntry("filename1");
        output.putNextEntry(e);
        output.write("helloWorld1".getBytes());
        output.closeEntry();

        output.finish();
        output.flush();
    }

    private static Cipher createCipher(int mode, String password) throws Exception {
        String alg = "PBEWithSHA1AndDESede"; //BouncyCastle has better algorithms
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(alg);
        SecretKey secretKey = keyFactory.generateSecret(keySpec);

        Cipher cipher = Cipher.getInstance("PBEWithSHA1AndDESede");
        cipher.init(mode, secretKey, new PBEParameterSpec("saltsalt".getBytes(), 2000));

        return cipher;
    }

    private static void read(String password) throws Exception {
        InputStream target = new FileInputStream("out.zip");
        target = new CipherInputStream(target, createCipher(Cipher.DECRYPT_MODE, password));
        ZipInputStream input = new ZipInputStream(target);
        ZipEntry entry = input.getNextEntry();
        while (entry != null) {
            System.out.println("Entry: "+entry.getName());
            System.out.println("Contents: "+toString(input));
            input.closeEntry();
            entry = input.getNextEntry();
        }
    }

    private static String toString(InputStream input) throws Exception {
        byte [] data = new byte[1024];
        StringBuilder result = new StringBuilder();

        int bytesRead = input.read(data);
        while (bytesRead != -1) {
            result.append(new String(data, 0, bytesRead));
            bytesRead = input.read(data);
        }

        return result.toString();
    } 
}
