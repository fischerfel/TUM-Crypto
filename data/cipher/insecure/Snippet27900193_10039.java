 class decrypt{
 public static void main(String[] args) {
    try {
   String FileName1 = "D:/ashok/encrypted.txt";
        String FileName2 = "D:/ashok/decrypted.txt";

        KeyGenerator KeyGen = KeyGenerator.getInstance("AES");
        KeyGen.init(128);

        SecretKey SecKey = KeyGen.generateKey();

        Cipher AesCipher =  Cipher.getInstance("AES");
        byte[] cipherrText = Files.readAllBytes(Paths.get(FileName1));

        AesCipher.init(Cipher.DECRYPT_MODE, SecKey);
        byte[] bytePlainText = AesCipher.doFinal(cipherrText);
        Files.write(Paths.get(FileName2), bytePlainText);  }}
