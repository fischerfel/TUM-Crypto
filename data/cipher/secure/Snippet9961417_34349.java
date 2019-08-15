public class ClassMain {
    public static void main(String[]args) throws Exception {
        String ST = "Ebenezersawesome";
        byte[] plainText = "ST".getBytes("UTF8");
        // Generating RSA Key
        System.out.println("\nStart generating RSA key");
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(4096);
        KeyPair key = keyGen.generateKeyPair();
        System.out.println("Finish generating RSA key");
        //
        // Creates an RSA Cipher object (specifying the algorithm, mode, and
        // padding).
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        //
        // Print the provider information
        System.out.println("\n" + cipher.getProvider().getInfo());
        System.out.println("\nStart encryption");
        //
        // Initializes the Cipher object.
        cipher.init(Cipher.ENCRYPT_MODE, key.getPublic());
        //
        // Encrypt the plaintext using the public key
        byte[] cipherText = cipher.doFinal(plainText);
        System.out.println("Finish encryption: ");
        System.out.println(new String(cipherText, "UTF8"));
        System.out.println("\nStart decryption");
        //
        // Initializes the Cipher object.
        cipher.init(Cipher.DECRYPT_MODE, key.getPrivate());
        //
        // Decrypt the ciphertext using the private key
        byte[] newPlainText = cipher.doFinal(cipherText);
        System.out.println("Finish decryption: ");
        System.out.println(new String(newPlainText, "UTF8"));
    }
}
