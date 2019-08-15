byte[] modulusBytes = Base64.decode("2rRVVVFJRbH/wAPDtnwZwu+nxU+AZ6uXxh/sW+AMCBogg7vndZsnRiHoLttYYPqOyOhfgaBOQogrIfrKL4lipK4m52SBzw/FfcM9DsKs/rYR83tBLiIAfgdnVjF27tZID+HJMFTiI30mALjr7+tfp+2lIACXA1RIKTk7S9pDmX8=");
byte[] exponentBytes = Base64.decode("AQAB");
BigInteger modulus = new BigInteger(1, modulusBytes );
BigInteger exponent = new BigInteger(1, exponentBytes);

RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus, exponent);
KeyFactory fact = KeyFactory.getInstance("RSA");
PublicKey pubKey = fact.generatePublic(rsaPubKey);

Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher.init(Cipher.ENCRYPT_MODE, pubKey);

byte[] plainBytes = new String("big kitty dancing").getBytes("UTF-8");
byte[] cipherData = cipher.doFinal( plainBytes );
String encryptedString = Base64.encode(cipherData);
return encryptedString;
