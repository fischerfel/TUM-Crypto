   private static byte[] decrypt(byte[] inpBytes, PrivateKey key,String xform) throws Exception{
Cipher cipher = Cipher.getInstance(xform);
cipher.init(Cipher.DECRYPT_MODE, key);
return cipher.doFinal(inpBytes);
}

//fetch private key           
byte[] prvk1 = new byte[(int)new File("C:\\...\\"+T_Username.getText()+"\\private_"+ T_Username.getText()).length()]; 

//make it from bytes to private key
  KeyFactory kf= KeyFactory.getInstance("RSA");
  PrivateKey prvk=kf.generatePrivate(new PKCS8EncodedKeySpec(prvk1));

//fetch cipher
 byte[] encBytes = new byte[(int)new File("C:\\...\\"+T_Username.getText()+"\\hash_"+ T_Username.getText()).length()]; 

//decrypt with our password given from user
    String xform = "RSA";

         byte[] decBytes = decrypt(encBytes,prvk, xform);

        boolean expected = java.util.Arrays.equals(password, decBytes);
        System.out.println("Test " + (expected ? "SUCCEEDED!" : "FAILED!"));
