cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher.init(Cipher.DECRYPT_MODE, privateKey);
byte[] newPlainText = cipher.doFinal(arg.getBytes());
