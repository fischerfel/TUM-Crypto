   //string encrypted contains the string of the encoded characters. 

     String encrypted = intent.getStringExtra("SCAN_RESULT");

     //converting the string into a byte array          
     byte[] byteEncrypted = encrypted.getBytes();

     //instantiating the AES cipher object
     Cipher cipher = Cipher.getInstance("AES");

     //Predefined public-key 

     byte[] skey = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a,  0x0b, 0x0c, 0x0d, 0x0e, 0x0f };

     //creating a secretKeySpec       
     SecretKeySpec skeyspec = new SecretKeySpec(skey, "AES");

 //initializing the cipher to Decrypt               
     cipher.init(Cipher.DECRYPT_MODE, skeyspec);
  final byte[] decrypt  = cipher.doFinal(byteEncrypted);

 //decrypting the string                
 String contents = new String(decrypt, "UTF-8");
