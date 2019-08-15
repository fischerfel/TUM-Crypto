byte[] modulusBytes = Base64.decode(Modoutput.getBytes("UTF-8"),
        Base64.DEFAULT);
byte[] exponentBytes = Base64.decode(Expoutput.getBytes("UTF-8"),
        Base64.DEFAULT);
BigInteger e = new BigInteger(1, exponentBytes);
BigInteger m = new BigInteger(1, modulusBytes);
RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
KeyFactory fact = KeyFactory.getInstance("RSA");
PublicKey pubKeyn = fact.generatePublic(keySpec);

Log.i("Publickey", pubKeyn.toString());
Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
cipher.init(Cipher.ENCRYPT_MODE, pubKeyn);
byte[] encryptedByteData = cipher.doFinal(byteData);
String outputEncrypted = Base64.encodeToString(encryptedByteData,
        Base64.NO_WRAP);

Log.i("Encrypteddata", outputEncrypted);
