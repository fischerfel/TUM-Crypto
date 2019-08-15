public static PrivateKey readPrivateKeyFromFile(File file, String chaveSecreta) {
    try {
        SecureRandom r = new SecureRandom(chaveSecreta.getBytes());
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        keyGen.init(56, r);
        Key key = keyGen.generateKey();

        byte[] privateKeyBytes = decryptPKFile(file, key);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        PrivateKey privateKey = null;
        try {
            privateKey = keyFactory.generatePrivate(privateKeySpec);
        } catch (InvalidKeySpecException e) {
            JOptionPane.showMessageDialog(null, "Erro 01, tente mais tarde");
        }
        return privateKey;
    } catch (NoSuchAlgorithmException e) {
        JOptionPane.showMessageDialog(null, "Erro 02, tente mais tarde");
    }
    return null;
}

public static byte[] decryptPKFile(File file, Key key){
    try{
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        byte[] cipherText = readBytes(file);
        cipher.init(Cipher.DECRYPT_MODE, key);
        System.out.println(cipher);
        System.out.println(cipherText);
        byte[] text = cipher.doFinal(cipherText);
        return text;
    }catch(Exception e){
        e.printStackTrace();
        return null;
    }
}

public static byte[] readBytes(File file) {
    try {
        FileInputStream fs = new FileInputStream(file);
        byte content[] = new byte[(int) file.length()];
        fs.read(content);
        return content;
    } catch (FileNotFoundException e) {
        System.out.println("Arquivo não encontrado!");
        e.printStackTrace();
    } catch (IOException ioe) {
        System.out.println("Erro ao ler arquivo!");
        ioe.printStackTrace();
    }
    return null;
}
