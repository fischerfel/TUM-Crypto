String pass = "password";
String strEncryInfoData="";
try {
     KeyFactory keyFac = KeyFactory.getInstance("RSA");
     KeySpec keySpec = new X509EncodedKeySpec(Base64.decode(response.trim().getBytes(), Base64.DEFAULT));
     Key publicKey = keyFac.generatePublic(keySpec);

     cipher = Cipher.getInstance("RSA");
     cipher.init(Cipher.ENCRYPT_MODE, publicKey);
     byte[] cipherText = cipher.doFinal(pass.getBytes());
     strEncryInfoData = new String(Base64.encode(cipherText,Base64.DEFAULT));
} catch (Exception e){

}
