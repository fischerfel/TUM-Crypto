SecretKeySpec spec = getKeySpec();
Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.ENCRYPT_MODE, spec);
BASE64Encoder enc = new BASE64Encoder();
String hexString = stringToHex(text);
>return enc.encode(cipher.doFinal(hex2byte(hexString)));
