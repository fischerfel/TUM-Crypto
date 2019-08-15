private static final byte[] encryptionKey = new byte[]{ 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F };

byte[] iv = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

IvParameterSpec ips = new IvParameterSpec(iv);
Cipher aesCipher = Cipher.getInstance("AES/CTR/NoPadding");
SecretKeySpec aeskeySpec = new SecretKeySpec(encryptionKey, "AES");
aesCipher.init(Cipher.DECRYPT_MODE, aeskeySpec, ips);
FileInputStream is = new FileInputStream(in);
CipherOutputStream os = new CipherOutputStream(new FileOutputStream(out), aesCipher);       
copy(is, os);       
os.close();
