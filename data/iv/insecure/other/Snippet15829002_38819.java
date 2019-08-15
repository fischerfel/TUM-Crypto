byte[] rawKey = new byte[32];

System.arraycopy("A01BD1BE-9D28-11E2-A12E-48086188709B".getBytes("UTF-8"), 0, rawKey, 0, 32);

Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

final byte[] iv = new byte[16];
Arrays.fill(iv, (byte) 0x00);
IvParameterSpec ivSpec = new IvParameterSpec(iv);
SecretKeySpec keySpec = new SecretKeySpec(rawKey, "AES");

cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
byte[] results = cipher.doFinal(clearTextByte);
String result = Base64.encodeToString(results, Base64.DEFAULT);
