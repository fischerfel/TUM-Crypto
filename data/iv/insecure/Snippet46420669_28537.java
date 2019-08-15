Path path = null;
String encryptionKey = null;
Cipher cipher = null;
SecretKeySpec key = null;
byte[] enc1;


path = FileSystems.getDefault().getPath("c:\\temp\\encryption\\SakisEnc.txt", "");
cipherText = Files.readAllBytes(path);

encryptionKey = "0123456789012345";   
cipher = Cipher.getInstance("AES/CBC/NoPadding");   
key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(encryptionKey.getBytes()));
dec1 = cipher.doFinal(cipherText);
