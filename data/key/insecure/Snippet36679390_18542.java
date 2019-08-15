FileInputStream fis = new FileInputStream("dataPath/data");
SecretKeySpec sks = new SecretKeySpec("password".getBytes(), "AES");
Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.DECRYPT_MODE, sks);
CipherInputStream cis = new CipherInputStream(fis, cipher);
