    public void EncryptDemo(){
    try {
        FileInputStream keyfis = new FileInputStream("mainkey.key");
        byte[] encodedKey = new byte[keyfis.available()];
        keyfis.read(encodedKey);
        keyfis.close();
        Key KeyFromFile = new SecretKeySpec(encodedKey, "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        String text=txtToEncryptData.getText(), output;
        cipher.init(Cipher.ENCRYPT_MODE, KeyFromFile);
        DataDemo = cipher.doFinal(text.getBytes());
        InsertIntoDataBase();
        //I store it as varbinary in database
    } catch (FileNotFoundException ex) {
        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchPaddingException ex) {
        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InvalidKeyException ex) {
        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalBlockSizeException ex) {
        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
    } catch (BadPaddingException ex) {
        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
    }
}

    public void DecryptDemo(){
    try {
        FileInputStream keyfis = new FileInputStream("mainkey.key");
        byte[] encodedKey = new byte[keyfis.available()];
        keyfis.read(encodedKey);
        keyfis.close();
        Key KeyFromFile = new SecretKeySpec(encodedKey, "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, KeyFromFile);
        String sql = "{call SelectAll}";
        CallableStatement call = conn.prepareCall(sql);
        call.execute();
        ResultSet result = call.getResultSet();
        DefaultListModel model = new DefaultListModel();
        while(result.next()){
            DataDemo = result.getBytes("TestData");
            byte[] plainText = cipher.doFinal(DataDemo);
            String after = new String(plainText);
            model.addElement(after);
        }
        lstDecryptResult.setModel(model);
    } catch (SQLException ex) {
        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
    } catch (FileNotFoundException ex) {
        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchPaddingException ex) {
        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InvalidKeyException ex) {
        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalBlockSizeException ex) {
        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
    } catch (BadPaddingException ex) {
        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
    }
}
