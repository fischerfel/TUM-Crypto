public static void main(String[] args) throws Exception
{
    // Initialise secret key with predefined byte array [] like below. I
    // have used simple string to array method to generate 16 byte array.
    // AES Key must be minimum 16 bytes.
    // Now you can put this byte array some where is .SO file.
    // Generate new Key using this byte []
    // Then you can generate a key using device specific information at
    // first boot up.
    // Use second key to encrypt data and first key to encrypt the second
    // key
    // I Hope it clears all the doubts
    SecretKey key = new SecretKeySpec("ABCDEFGHIJKLMNOP".getBytes(), "AES");
    System.out.println(Arrays.toString(key.getEncoded()));
    // Initialise Cipher with AES Algorithm
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    // Set The Encrypt Mode
    cipher.init(Cipher.ENCRYPT_MODE, key);
    // Encrypt some bytes
    byte[] encrypted = cipher.doFinal("ABCDEFGH".getBytes());
    // Print it to vefiry
    System.out.println(Arrays.toString(encrypted));

    // Get The IV
    byte[] iv = cipher.getIV();
    System.out.println(iv.length);
    // Now why storing you can create structure like [16 IV][Encrypted Data]
    // And while decrypting you can read first [16] bytes IV and then
    // decrypt remaining bytes

    //byte[] iv = new byte[16];
    // System.arraycopy(encrypted, 0, iv, 0, 16)
    //Copy remaining bytes to decrypt


    // set cipher to decrypt mode

    cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(iv));

    // decrypt it
    byte[] decrypted = cipher.doFinal(encrypted);
    System.out.println(new String(decrypted));

}
