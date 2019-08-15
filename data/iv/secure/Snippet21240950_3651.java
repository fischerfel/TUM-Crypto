Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
byte[] keyBytes = new byte[16];
byte[] b = passphare.getBytes("UTF-8");
int len = b.length;
if (len > keyBytes.length) {
    len = keyBytes.length;
}

System.arraycopy(b, 0, keyBytes, 0, len);
SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
cipher.init(opmode, keySpec, ivSpec);

cipher.doFinal(textToEncrypt.getBytes("UTF-8"));
