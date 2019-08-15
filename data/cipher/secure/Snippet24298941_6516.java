   byte[] modulusBytes = Base64.decode("xTSiS4+I/x9awUXcF66Ffw7tracsQfGCn6g6k/hGkLquHYMFTCYk4mOB5NwLwqczwvl8HkQfDShGcvrm47XHKUzA8iadWdA5n4toBECzRxiCWCHm1KEg59LUD3fxTG5ogGiNxDj9wSguCIzFdUxBYq5ot2J4iLgGu0qShml5vwk=");
   byte[] exponentBytes = Base64.decode("AQAB");
   BigInteger modulus = new BigInteger(1, modulusBytes );               
   BigInteger exponent = new BigInteger(1, exponentBytes);

   RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus, exponent);
   KeyFactory fact = KeyFactory.getInstance("RSA");
   PublicKey pubKey = fact.generatePublic(rsaPubKey);

   Cipher cipher = Cipher.getInstance("RSA");
   cipher.init(Cipher.ENCRYPT_MODE, pubKey);

   byte[] plainBytes = new String("big kitty dancing").getBytes("UTF-8");
   byte[] cipherData = cipher.doFinal( plainBytes );
   String encryptedString = Base64.encodeBytes(cipherData);
