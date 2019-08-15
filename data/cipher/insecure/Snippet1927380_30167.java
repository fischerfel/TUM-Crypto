public class AES {

    /**
     * Turns array of bytes into string
     * 
     * @param buf
     *            Array of bytes to convert to hex string
     * @return Generated hex string
     */

    public static void main(String[] args) throws Exception {

        File file = new File("testxls.xls");

        byte[] lContents = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(lContents);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(256); // 192 and 256 bits may not be available
            // Generate the secret key specs.
            SecretKey skey = kgen.generateKey();
            // byte[] raw = skey.getEncoded();
            byte[] raw = "aabbccddeeffgghhaabbccddeeffgghh".getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(lContents);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] original = cipher.doFinal(lContents);
            FileOutputStream f1 = new FileOutputStream("testxls_java.xls");
            f1.write(original);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
