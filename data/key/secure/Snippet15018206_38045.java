public static void decryptFile(String keyString, String fileName){
    try {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        SecretKey key = (SecretKey) new SecretKeySpec(
            keyString.getBytes(), "AES");// kgen.generateKey();

        AESEncrypter encrypter = new AESEncrypter(key);

        encrypter.decrypt(new FileInputStream(
            new java.io.File("").getCanonicalFile() +
            File.separator + "Received"+
            File.separator + fileName),
            new FileOutputStream(new java.io.File("").getCanonicalFile() +
            File.separator + "Decrypted" + 
            File.separator + fileName));
    } catch (Exception e) {
        e.printStackTrace();
    }
}
