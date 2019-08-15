public class CipherStreams {
    public static void main(String[] args) {
        try {
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            Key k = keygen.generateKey();

            Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
            aes.init(Cipher.ENCRYPT_MODE, k);
            String fileName = "Encrypted.txt";
            FileOutputStream fs = new FileOutputStream(fileName);
            CipherOutputStream out = new CipherOutputStream(fs, aes);
            out.write("[Hello:Okay]\nOkay".getBytes());
            out.flush();
            out.close();

            Cipher aes2 = Cipher.getInstance("AES/ECB/PKCS5Padding");
            aes2.init(Cipher.DECRYPT_MODE, k);

            FileInputStream fis = new FileInputStream(fileName);
            CipherInputStream in = new CipherInputStream(fis, aes2);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] b = new byte[1024];
            int numberOfBytedRead;
            while ((numberOfBytedRead = in.read(b)) >= 0) {
                baos.write(b, 0, numberOfBytedRead);
            }
            System.out.println(new String(baos.toByteArray()));
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IOException ex) {
            ex.printStackTrace();
            ;
        }
    }
}
