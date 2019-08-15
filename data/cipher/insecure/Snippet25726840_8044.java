public void crypt() throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IOException{

    Cipher cipher = Cipher.getInstance("AES");
    KeyGenerator keyGen = KeyGenerator.getInstance("AES");
    SecretKey secKey = keyGen.generateKey();
    byte[] encoded = secKey.getEncoded();
    this.setCodeCrypt(encoded);

    cipher.init(Cipher.ENCRYPT_MODE, secKey);

    String cleartextFile = this.lien;
    String ciphertextFile = "crypted img.jpg";

    FileInputStream fis = new FileInputStream(cleartextFile);
    FileOutputStream fos = new FileOutputStream(ciphertextFile);
    CipherOutputStream cos = new CipherOutputStream(fos, cipher);

    int i;
    while ((i = fis.read()) != -1) {
        cos.write(i);
    }
    cos.close();
}

    // Decrypt
public void decrypt() throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IOException{
    try {
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/papiersadmin";
        String user = "postgres";
        String passwd = "postgresql";
        java.sql.Connection conn = DriverManager.getConnection(url, user,passwd);
        Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

        // loading a picture knowing its path (lien)

        ResultSet result = state.executeQuery("SELECT * FROM image WHERE lien = '"+this.lien+"'");
        while(result.next()){
        setCodeCrypt(result.getObject(6).toString().getBytes());}
        state.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    Cipher cipher = Cipher.getInstance("AES");
    SecretKey originalKey = new SecretKeySpec(codeCrypt, 0, codeCrypt.length, "AES");
    cipher.init(Cipher.DECRYPT_MODE, originalKey);

    String cleartextFile = "decrypted img.jpg";
    String ciphertextFile = this.lien;

    FileInputStream fis = new FileInputStream(ciphertextFile);
    FileOutputStream fos = new FileOutputStream(cleartextFile);
    CipherOutputStream cos = new CipherOutputStream(fos, cipher);

    int i;
    while ((i = fis.read()) != -1) {
        cos.write(i);
    }
    cos.close();
}
