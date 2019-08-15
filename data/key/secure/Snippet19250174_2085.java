public class Test {
    public static void main(String[] args) throws Exception {
        String message = "This string contains a secret message.";

        // generate a key
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(128);
        byte[] key = keygen.generateKey().getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

        byte[] iv = { 0, 1, 0, 2, 0, 3, 0, 4, 0, 5, 0, 6, 0, 7, 0, 8 };
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        // initialize the cipher for encrypt mode
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);

        // encrypt the message
        byte[] encrypted = cipher.doFinal(message.getBytes());
        System.out.println("Ciphertext: " + hexEncode(encrypted) + "\n");

        // Write IV
        FileOutputStream fs = new FileOutputStream(new File("paramFile"));
        BufferedOutputStream bos = new BufferedOutputStream(fs);
        bos.write(iv);
        bos.close();

        // Read IV
        byte[] fileData = new byte[16];
        DataInputStream dis = null;

        dis = new DataInputStream(new FileInputStream(new File("paramFile")));
        dis.readFully(fileData);
        if (dis != null) {
            dis.close();
        }

        // reinitialize the cipher for decryption
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(fileData));

        // decrypt the message
        byte[] decrypted = cipher.doFinal(encrypted);
        System.out.println("Plaintext: " + new String(decrypted) + "\n");
    }

    [...]
}
