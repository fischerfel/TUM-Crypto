private static byte[] encrypt(byte[] inpBytes, PublicKey key,
String xform) throws Exception {
 Cipher cipher = Cipher.getInstance(xform);
 cipher.init(Cipher.ENCRYPT_MODE, key);
return cipher.doFinal(inpBytes);
}


 String xform = "RSA";
 // Generate a key-pair
 KeyPairGenerator kpg = null;
        try {
            kpg = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Efarmogi_1.class.getName()).log(Level.SEVERE, null, ex);
        }
 kpg.initialize(512); // 512 is the keysize.
 KeyPair kp = kpg.generateKeyPair();
 /create public and private key
 PublicKey pubk = kp.getPublic();
 PrivateKey prvk = kp.getPrivate();

 //password from user 
 String password = T_Password.getText();

byte[] dataBytes = password.getBytes();
//create cipher
byte[] encBytes = null;
        try {
            encBytes = encrypt(dataBytes, pubk, xform);
        } catch (Exception ex) {
            Logger.getLogger(Efarmogi_1.class.getName()).log(Level.SEVERE, null, ex);
        }

//storing
//cipher
  FileOutputStream cipher = null;
        try {
            cipher = new FileOutputStream( "Xrhstes\\"+T_Username.getText()+"\\hash_"+T_Username.getText());

            cipher.write(encBytes);//write with bytes

            cipher.close();
        } catch (IOException ex) {
            Logger.getLogger(Efarmogi_1.class.getName()).log(Level.SEVERE, null, ex);
        }

//private key
 byte[] key2 = prvk.getEncoded();
            FileOutputStream keyfos2 = null;
        try {
            keyfos2 = new FileOutputStream("Xrhstes\\"+T_Username.getText()+"\\private_"+ T_Username.getText()+".pem");

            keyfos2.write(key2);

            keyfos2.close();
