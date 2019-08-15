byte[] IV = { 65, 1, 2, 23, 4, 5, 6, 7, 32, 21, 10, 11, 12, 13, 84, 45 };
byte[] KEY = { 0, 42, 2, 54, 4, 45, 6, 7, 65, 9, 54, 11, 12, 13, 60, 15 };
byte baData[] = new byte[1024];
int iRead = 0;

SecretKeySpec key = new SecretKeySpec(KEY, "AES/CBC/PKCS5Padding");
Cipher cipher = Cipher.getInstance ("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV));

File file = new File("/sdcard", "SAMPLE.txt");

FileInputStream in = new FileInputStream(file);
iRead = in.read(baData, 0, baData.length);

String strResult = new String(cipher.doFinal(baData, 0, baData.length));
