class TCPServer {
public static void main(String argv[]) throws Exception {
    String password = null;
    String capitalizedSentence;
    ServerSocket welcomeSocket = new ServerSocket(6789);

    while (true) {
        Socket connectionSocket = welcomeSocket.accept();
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        password = "Passcode";
        byte[] salt = new byte[64];
        Random rnd = new Random();
        rnd.nextBytes(salt);
        byte[] data = deriveKey(password, salt, 64);
        byte [] EncyptedText = inFromClient.readLine().getBytes();
        System.out.println("Received Encrypted message " + EncyptedText);
        SecretKey desKey = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(data));
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, desKey);
        // Decrypt the text
        System.out.println("Text Received " + EncyptedText);
        byte[] textDecrypted = cipher.doFinal(EncyptedText);
        System.out.println("Text Decryted : " + new String(textDecrypted));

    }
}

public static byte[] deriveKey(String password, byte[] salt, int keyLen) {
        SecretKeyFactory kf = null;
        try {
            kf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        KeySpec specs = new PBEKeySpec(password.toCharArray(), salt, 1024, keyLen);
        SecretKey key = null;
        try {
            key = kf.generateSecret(specs);
        } catch (InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return key.getEncoded();
}
}
