String skey = "6543210987654321";
String siv = "1234567890123456";
String sinput = "Encrypt_this_text";

byte[] key = skey.getBytes("UTF8");
byte[] iv = siv.getBytes("UTF8");
byte[] input = sinput.getBytes("UTF8");

Cipher cipher = Cipher.getInstance("AES/CBC/ZeroBytePadding");
SecretKeySpec keyspec = new SecretKeySpec(key, "AES" );
IvParameterSpec ivparams = new IvParameterSpec(iv);
cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivparams);

byte[] encrypted = cipher.doFinal(input);
