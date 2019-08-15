String encryptionKey = null;
Cipher cipher = null;
SecretKeySpec key = null;
byte[] enc1;
byte[] dec1;

path = FileSystems.getDefault().getPath("c:\\temp\\encryption\\SakisEnc.txt", "");
cipherText = Files.readAllBytes(path);

encryptionKey = "0123456789012345";
cipher = Cipher.getInstance("AES/CBC/PKCS5Padding") ;
key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(encryptionKey.getBytes("UTF-8")));
dec1 = cipher.doFinal(cipherText);
