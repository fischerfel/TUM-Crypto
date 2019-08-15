public static void main(String[] args) throws Exception {
    // Generate AES Key
    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    SecretKey myAesKey = keyGenerator.generateKey();

    Cipher aesCipher = Cipher.getInstance("AES");

    String text = "11111110001100110011011111111011011111111101000111000101111111111111111001011110110001011111110111111001110110011100110111011111101111100111101";

    // ENCRYPT the text
    aesCipher.init(Cipher.ENCRYPT_MODE, myAesKey);
    byte[] textEncrypted = aesCipher.doFinal(text.getBytes());

    // Output results
    System.out.println("Text [Byte Format]: " + text);
    System.out.println("Text : " + new String(text));
    System.out.println("Text Encrypted: " + textEncrypted);

    // Write the 'text' to a file
    File encryptFileResult = new File("TestFiles/exampleOrig.txt");
    if (!encryptFileResult.exists()) {
        encryptFileResult.createNewFile();
    } else {
        encryptFileResult.delete();
        encryptFileResult.createNewFile();
    }

    FileWriter encryptFileWriter = new FileWriter(encryptFileResult.getAbsoluteFile());
    BufferedWriter bufferedWriter = new BufferedWriter(encryptFileWriter);

    bufferedWriter.write(new String(textEncrypted));
    bufferedWriter.close();

    // Grab all bytes from the 'exampleOrig.txt' file
    byte[] encryptedBytes = Files.readAllBytes(encryptFileResult.toPath());

    // DECRYPT the text
    aesCipher.init(Cipher.DECRYPT_MODE, myAesKey);
    byte[] textDecrypted = aesCipher.doFinal(encryptedBytes);

    System.out.println("Text Decrypted: " + new String(textDecrypted));
}
