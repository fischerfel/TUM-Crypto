public static void main(String[] args) throws Exception
{
    String input = JOptionPane.showInputDialog(null, "Enter your String");
    System.out.println("Plaintext: " + input + "\n");

    // Generate a key
    KeyGenerator keygen = KeyGenerator.getInstance("AES");
    keygen.init(128); 
    byte[] key = keygen.generateKey().getEncoded();
    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

    // Generate IV randomly
    SecureRandom random = new SecureRandom();
    byte[] iv = new byte[16];
    random.nextBytes(iv);
    IvParameterSpec ivspec = new IvParameterSpec(iv);

    // Initialize Encryption Mode
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);

    // Encrypt the message
    byte[] encryption = cipher.doFinal(input.getBytes());
    System.out.println("Ciphertext: " + encryption + "\n"); //

    // Initialize the cipher for decryption
    cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivspec);

    // Decrypt the message
    byte[] decryption = cipher.doFinal(encryption);
    System.out.println("Plaintext: " + new String(decryption) + "\n");
}
