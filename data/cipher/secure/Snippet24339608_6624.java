try{
String strModulus = "tr82UfeGetV7yBKcOPjFTWs7pHqqr/5YKKWMUZ/HG4HnCmWrZsOhuR1FBnMZ/g2YiosoSlu0zd7Ukz9lX7wv2RLfWXfMvZYGpAAvfYWwzbyQ2i1q+tKE/thgKNscoSRellDD+uJcYn1H4hnaudVyYJH9miVhOKhKlExMzw8an6U=";
String strExponent = "AQAB";
byte[] modulusBytes = Base64.decode(strModulus, Base64.DEFAULT);
byte[] exponentBytes = Base64.decode(strExponent, Base64.DEFAULT);

BigInteger modulus = new BigInteger(1, modulusBytes );               
BigInteger exponent = new BigInteger(1, exponentBytes);

RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus, exponent);
KeyFactory fact = KeyFactory.getInstance("RSA/ECB/PKCS1Padding");
PublicKey pubKey = fact.generatePublic(rsaPubKey);    

Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher.init(Cipher.ENCRYPT_MODE, pubKey);   

byte[] plainBytes = new String("Manchester United").getBytes("UTF-8");
byte[] cipherData = cipher.doFinal(plainBytes); 
encryptedString = Base64.encodeToString(cipherData, Base64.DEFAULT);
}
catch(Exception e){
Log.e("Error", e.toString());
}

return encryptedString;
