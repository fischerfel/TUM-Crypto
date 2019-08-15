byte[] dectyptedText = null;
Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher.init(Cipher.DECRYPT_MODE, key);
dectyptedText = cipher.doFinal(text);
return dectyptedText;
