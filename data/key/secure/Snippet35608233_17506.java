    public static void main(String[] args) throws Exception {
    // TODO Auto-generated method stub
    generateKey(clef);

    CryptingWithSave(Cipher.ENCRYPT_MODE,"db/db.csv",publicKey,"db/db.csv_encrypted");
    decrypt_file("db/db.csv_encrypted",publicKey);
    System.out.println(tab);
}


    public static void generateKey (String clef) throws NoSuchAlgorithmException{
     final KeyGenerator keyGen = KeyGenerator.getInstance("AES");
     keyGen.init(128);
     final SecretKey key = keyGen.generateKey();

    publicKey= new SecretKeySpec(key.getEncoded(),"AES");
}

    public static void CryptingWithSave (int Crypting_METHOD,String inputFile, SecretKeySpec clef, String outputFile) throws NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, Exception{
   Cipher cipher = Cipher.getInstance("AES");
   cipher.init(Crypting_METHOD, clef);

        FileInputStream fis = new FileInputStream(inputFile);
        FileOutputStream fos = new FileOutputStream(outputFile);

        byte[] input = new byte[64];
        int bytesRead;

        while ((bytesRead = fis.read(input)) != -1) {
            byte[] output = cipher.update(input, 0, bytesRead);
            if (output != null)
                fos.write(output);
        }

        byte[] output = cipher.doFinal();
        if (output != null)
            fos.write(output);

        fis.close();
        fos.flush();
        fos.close();
}

public static void decrypt_file (String inputFile, SecretKeySpec clef) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException{
    Cipher cipher2 = Cipher.getInstance("AES");
       cipher2.init(Cipher.DECRYPT_MODE, clef);
        FileInputStream fis = new FileInputStream(inputFile);
        byte[] input = new byte[64];
        int bytesRead;

        while ((bytesRead = fis.read(input)) != -1) {
            byte[] output = cipher2.update(input, 0, bytesRead);
            if (output != null)
                tab=tab.concat(new String(output));
        }

        byte[] output = cipher2.doFinal();
        if (output != null)
            tab=tab.concat(new String(output));
        fis.close();
}
