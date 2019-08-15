// Create a new key to encrypt and decrypt the file
byte[] key = "password".getBytes();

// Get a cipher object in encrypt mode 
Cipher cipher = null;
try {
    DESKeySpec dks = new DESKeySpec(key);
    SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
    SecretKey desKey = skf.generateSecret(dks);
    cipher = Cipher.getInstance("DES");
    cipher.init(Cipher.ENCRYPT_MODE, desKey);
} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException ex) {
    System.err.println("[CRITICAL] Incryption chiper error");
}

// Encrypt the file
try {
    new ObjectOutputStream(new CipherOutputStream(new FileOutputStream("test"), cipher)).writeObject("test text");
} catch (IOException e) {
    System.err.println("[CRITICAL] Error encrypting data: " + e.getMessage());
    e.printStackTrace();
}

// Get a cipher object in decrypt mode
try {
    DESKeySpec dks = new DESKeySpec(key);
    SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
    SecretKey desKey = skf.generateSecret(dks);
    cipher = Cipher.getInstance("DES");
    cipher.init(Cipher.DECRYPT_MODE, desKey);
} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException ex) {
    System.err.println("[CRITICAL] Incryption chiper error");
}

// Decrypt the file
try {
    // This is the line that throws the exception
    System.out.println((String) new ObjectInputStream(new CipherInputStream(new FileInputStream("test"), cipher)).readObject()); 
} catch (IOException | ClassNotFoundException e) {
    System.err.println("[CRITICAL] Error decrypting data: " + e.getMessage());
    e.printStackTrace();
}
