String smykey = "01234567891234567890123456789012";
String hellos = "hello";

SecretKeySpec key = new SecretKeySpec(smykey.getBytes(), "AES");

Cipher cipher =  Cipher.getInstance("AES");//("AES/ECB/PKCS7Padding");//("ECB");//("AES");
cipher.init(Cipher.ENCRYPT_MODE, key);

byte[] encrypted = cipher.doFinal(hellos.getBytes());
