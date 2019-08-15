byte[] key = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA".getBytes();
byte[] data = "x".getBytes();
byte[] iv = "1111111111111111".getBytes();
Cipher cipher = Cipher.getInstance("AES");
IvParameterSpec ivspec = new IvParameterSpec(iv);
cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), ivspec);
byte[] result = cipher.doFinal(data);
_print(result);
