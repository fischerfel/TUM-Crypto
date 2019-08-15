String firstTen = data.substring(0, 10);
byte[] decodedBytes = Base64.decode(firstTen, Base64.DEFAULT);
SecretKeySpec key = new SecretKeySpec(decodedBytes, "AES");
Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.DECRYPT_MODE, key);
byte[] original = cipher.doFinal(Message_to_Decrypt, Base64.DEFAULT));
