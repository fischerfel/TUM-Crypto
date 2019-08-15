Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
cipher.init(Cipher.DECRYPT_MODE, privateKey);

byte[] decrypted = cipher.doFinal(buffer);

String data = new String(decrypted);

System.out.println(data);
