Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
String keyString = "keykeykeykeykeykeykeykey";
byte[] keyBytes = keyString.getBytes("UTF-8");

cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes, "AES"),
        new IvParameterSpec(new byte[16]));
byte[] resultBytes = cipher.doFinal("Hallo Welt!".getBytes("UTF8"));

FileOutputStream out =
        new FileOutputStream(new File("encryptedFileJava"));
out.write(resultBytes);
out.close();
