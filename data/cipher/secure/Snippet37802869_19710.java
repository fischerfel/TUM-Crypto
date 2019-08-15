Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");

byte[] publicBytes = Base64.decode(Configs.PUBLIC_KEY.getBytes("UTF-8"),Base64.DEFAULT);
X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);

KeyFactory keyFactory = KeyFactory.getInstance("RSA");
PublicKey pubKey = keyFactory.generatePublic(keySpec);

cipher.init(Cipher.ENCRYPT_MODE, pubKey);

String plaintext = "test";

byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes("UTF-8"));
String chipertext = Base64.encodeToString(encryptedBytes,Base64.DEFAULT);
Log.d(TAG,"encrypted (chipertext) = " + chipertext);
