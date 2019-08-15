    public class AESFileDecryption {
        public static void main(String[] args) throws Exception {

    String password = "80 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01";

    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec keySpec = new PBEKeySpec(password.toCharArray());
    SecretKey tmp = factory.generateSecret(keySpec);
    SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

    // file decryption
    Cipher cipher = Cipher.getInstance();
    cipher.init(Cipher.DECRYPT_MODE,secret);
    FileInputStream fis = new FileInputStream("encryptedfile.des");
    FileOutputStream fos = new FileOutputStream("plainfile_decrypted.txt");
    byte[] in = new byte[64];
    int read;
    while ((read = fis.read(in)) != -1) {
        byte[] output = cipher.update(in, 0, read);
        if (output != null)
            fos.write(output);
    }

    byte[] output = cipher.doFinal();
    if (output != null)
        fos.write(output);
    fis.close();
    fos.flush();
    fos.close();
    System.out.println("File Decrypted");
}
