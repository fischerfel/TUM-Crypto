PublicKey publicKey = KeyFactory.getInstance("RSA").
                    generatePublic(new RSAPublicKeySpec(firstKeyInteger, secondKeyInteger));
// This always results in the public key OpenSSLRSAPublicKey{modulus=2b3b11f044.....58df890,publicExponent=10001}

Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA1ANDMGF1PADDING");
String stringToEncode = "EncodeThisString";
byte[] bytesToEncode = stringToEncode.getBytes("UTF-8");
cipher.init(cipher.PUBLIC_KEY, publicKey);
byte[] encrypted = cipher.doFinal(plain);
