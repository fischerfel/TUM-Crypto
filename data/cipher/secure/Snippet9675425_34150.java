   public void submitRegistration(String inputData)
   {
   //Decoding the encoded and encrypted message in the webservice      
   PrivateKey privateKey = getPrivateKeyFromKeyStore("abc");
   //I know I am using JDK proprietary classes, but I can easily replace this
   byte[] dataInBytes = new Base64Decoder().deodeBuffer(inputData)

   Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding"); 
   cipher.init(Cipher.DECRYPT_MODE, privateKey);
   byte[] decryptedData = cipher.doFinal(dataInBytes);          
   String original = new String(decryptedData, "UTF-8");
   System.out.println("Original Data : " + original);
}
  public String getPublicKey()
  {
    Certificate cert = getKeyStore().getCertificate("abc");
    byte[] encodedCert = cert.getEncoded();
    StringWriter sw = new StringWriter();
    sw.write("-----BEGIN PUBLIC KEY-----");
    sw.write(new Base64Encoder().encode(encodedCert));
    sw.write("-----END PUBLIC KEY-----");
    return sw.toString();
  }
