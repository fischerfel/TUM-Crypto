String password = "mypassword";
byte[] srcString = FileUtils.readFileToByteArray(new File("my_file.pdf"));
SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
aesCBC.init(Cipher.DECRYPT_MODE, key);
byte[] decrypted = aesCBC.doFinal(srcString);
