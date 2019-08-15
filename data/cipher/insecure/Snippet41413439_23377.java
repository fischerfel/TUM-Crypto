public class CipherStreams {
public static void main(String[] args) {
    try {
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        Key k = keygen.generateKey();

        Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
        aes.init(Cipher.ENCRYPT_MODE, k);
        FileOutputStream fs = new FileOutputStream("Encrypyed.txt");
        CipherOutputStream out = new CipherOutputStream(fs, aes);
        out.write("[Hello:Okay]\nOkay".getBytes());
        out.close();

        Cipher aes2 = Cipher.getInstance("AES/ECB/PKCS5Padding");
        aes2.init(Cipher.DECRYPT_MODE, k);

        FileInputStream fis = new FileInputStream("Encrypyed.txt");
        CipherInputStream in = new CipherInputStream(fis,aes2);
        byte[] b = new byte[8];
        int i = in.read(b);
        while(i!=-1) {
            System.out.print((char)i);
            i = in.read(b);
        }
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IOException ex) {
        Logger.getLogger(CipherStreams.class.getName()).log(Level.SEVERE, null, ex);
    }
}
}
