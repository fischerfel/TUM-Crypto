public  void initKey(String passwd, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException{

    byte[] localsalt = salt; 
   PBEKeySpec password = new PBEKeySpec(passwd.toCharArray(),localsalt, 1024,128);//, localsalt, 1000, 128);  //128bit enc aes
   SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWithMD5And128BitAES-CBC-OpenSSL","BC");  
   PBEKey key = (PBEKey) factory.generateSecret(password);  
   encKey = new SecretKeySpec(key.getEncoded(), "AES");
}


public   String txt2enc(String etxt) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {

       final Cipher cipher = Cipher.getInstance("AES");//AES       
       cipher.init(Cipher.ENCRYPT_MODE, encKey);      

       byte[] encrypted = cipher.doFinal((etxt).getBytes("UTF-8"));
       return Base64.encodeToString(encrypted, 0);
}

//decryption
public  String txt2dec(String dtxt) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{

    final Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, encKey);
    byte[] decrypt = cipher.doFinal(Base64.decode(dtxt, 0));
    return new String(decrypt);//return Base64.encodeToString(decrypt, 0);
}
