cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher.init(Cipher.DECRYPT_MODE, mRSAPublicKey);
final byte[] result = cipher.doFinal(data);
