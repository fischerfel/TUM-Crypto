private static String buildCipherEncryptCheck(String data){

    byte[] dataBytes = data.getBytes();
    String encryptedData = null;

    try {                               
        // Generate valid key
        KeyGenerator keygenerator = KeyGenerator.getInstance("AES");                

        keygenerator.init(128);
        SecretKey myKey = keygenerator.generateKey();           

        // Generate cipher encrypt
        Cipher cipher = Cipher.getInstance("AES/CFB1/PKCS5Padding", new BouncyCastleProvider());

        // Initialize the cipher for encryption
        cipher.init(Cipher.ENCRYPT_MODE, myKey);                

        // Encrypt the text
        byte[] textEncrypted = cipher.doFinal(dataBytes);
        encryptedData = new String(Base64.encode(textEncrypted));

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    }

    return encryptedData;
}   
