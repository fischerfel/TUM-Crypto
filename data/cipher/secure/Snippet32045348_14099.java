String s = "176byteofhexstring";
BigInteger Modulus = new BigInteger(s, 16);
String y = "03";
BigInteger Exponent = new BigInteger(y, 16);

RSAPublicKeySpec receiverPublicKeySpec = new RSAPublicKeySpec(Modulus, Exponent);
KeyFactory keyFactory = KeyFactory.getInstance("RSA");
RSAPublicKey receiverPublicKey = (RSAPublicKey)
    keyFactory.generatePublic(receiverPublicKeySpec);
Cipher rsaCipher = Cipher.getInstance("RSA/NONE/NoPadding","BC");
rsaCipher.init(Cipher.ENCRYPT_MODE, receiverPublicKey);
byte[] z = { 176 byte of cipher data };
byte[] m = rsaCipher.doFinal(z); 
