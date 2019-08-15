private static final byte[] KEY = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10};

int srcBuffSiz = 1024;
byte[] srcBuff = new byte[srcBuffSiz];
Arrays.fill(srcBuff, (byte)0x01);

SecretKeySpec skeySpec = new SecretKeySpec(KEY, "AES");
Cipher ecipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
ecipher.init(Cipher.ENCRYPT_MODE, skeySpec);
byte[] dstBuff = ecipher.doFinal(srcBuff);
int bytesEncrypted = dstBuff.length;
