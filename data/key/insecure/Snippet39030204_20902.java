byte[] buf = Base64.getDecoder().decode("AutMdzthDvPlE+UnhcHa2h4UZGPdme7t");
System.out.println(buf.length);
String key = "" + 2270457870L;
byte[] keyBytes = key.getBytes("UTF8");
System.out.println(keyBytes.length);

Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, "Blowfish"));

byte[] newBytes = cipher.doFinal(buf);
System.out.println(newBytes.length);
System.out.println(Arrays.toString(newBytes));
