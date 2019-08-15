static final BouncyCastleProvider PROVIDER = new BouncyCastleProvider();

public static void main(String[] args) throws Exception {
    KeyGenerator kg = KeyGenerator.getInstance("Threefish-1024", PROVIDER);
    kg.init(1024);
    SecretKey key = kg.generateKey();

    byte[] plaintext = "Hi! I'm cat!".getBytes();
    byte[] ciphertext = encrypt(key, plaintext);
    System.out.println(new String(decrypt(key, ciphertext)));
    // prints "Hi! I'm cat!"
}

static byte[] encrypt(SecretKey key, byte[] plaintext) throws Exception {
    return encryptOrDecrypt(true, key, plaintext);
}

static byte[] decrypt(SecretKey key, byte[] ciphertext) throws Exception {
    return encryptOrDecrypt(false, key, ciphertext);
}

static byte[] encryptOrDecrypt(boolean encrypt, SecretKey key, byte[] bytes) throws Exception {
    Cipher cipher = Cipher.getInstance("Threefish-1024/CBC/PKCS5Padding", PROVIDER);
    // note that we are creating a dummy iv parameter, in this case it
    // should be 128 bytes long, because if it's not an exception is raised
    cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, key, new IvParameterSpec(new byte[128]));
    return cipher.doFinal(bytes);
}
