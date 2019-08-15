public class encrypt {

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException {
        //Key is created and saved in File
        KeyGenerator keygenerator = KeyGenerator.getInstance("AES");
        SecretKey myDesKey = keygenerator.generateKey();
        String encodedKey = Base64.getEncoder().encodeToString(myDesKey.getEncoded());
        Path keypath = Paths.get("C:/xxx/key.txt");
        Path keyfile = Files.createFile(keypath);
        Files.write(keyfile, encodedKey.getBytes(), StandardOpenOption.WRITE);

        Cipher desalgCipher;
        desalgCipher = Cipher.getInstance("AES");
        desalgCipher.init(Cipher.ENCRYPT_MODE, myDesKey);

        Path target = Paths.get("C:/xxx/encrypted.txt");
        Path file = Files.createFile(target);

        Path path = Paths.get("test.txt");               
        try(InputStream is = Files.newInputStream(path);      
        CipherInputStream cipherIS = new CipherInputStream(is, desalgCipher);   
        BufferedReader reader = new BufferedReader(new InputStreamReader(cipherIS));){  
            String line;
            while((line = reader.readLine()) != null){
                System.out.println(line);
                Files.write(file, line.getBytes(), StandardOpenOption.WRITE);
            }
        }          
    }
}
