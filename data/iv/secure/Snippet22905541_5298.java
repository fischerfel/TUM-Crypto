// these are initialized in main
SecretKey key = KeyGenerator.getInstance("DES").generateKey();
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
            ecipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            dcipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
// catches ..

// it will take a string and the file that will have the encrypted strings
private static void encrypt(String s, OutputStream os) throws IllegalBlockSizeException, BadPaddingException {
        try {
            byte[] buf = s.getBytes();
            byte[] b = ecipher.doFinal(buf);
            os.write(b);
// this is to write a new line after writing each encrypted value and avoid overwriting
            os.write(System.getProperty("line.separator").getBytes()); 
            os.flush();
            os.close();
        }
    catch (IOException e) {
        System.out.println("I/O Error:" + e.getMessage());
    }
}
// this will take the file that has all of the encryptions
private static void decrypt(InputStream is) throws IllegalBlockSizeException, BadPaddingException {
    try {
        byte[] buf = new byte[is.available()];
        is.read(buf);
        byte[] decrypted = dcipher.doFinal(buf);  // THE CAUSE OF THE PROBLEM!!!!
        System.out.println(new String (decrypted));
        is.close();
    }
    catch (IOException e) {
        System.out.println("I/O Error:" + e.getMessage());
    }
