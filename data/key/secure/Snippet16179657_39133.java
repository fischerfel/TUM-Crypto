SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
IvParameterSpec ivspec = new IvParameterSpec(iv);

Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);

String plainText = "This is my plain text";
System.out.println("**plainText: " + plainText);

String saltedPlainText = plainText + UUID.randomUUID().toString().substring(0, 8);
byte[] encrypted = cipher.doFinal(saltedPlainText.getBytes());
String encryptedText = new String(new Hex().encode(encrypted));
System.out.println("**encryptedText: " + encryptedText);

cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivspec);

byte[] decrypted = cipher.doFinal(new Hex().decode(encryptedText.getBytes()));
saltedPlainText = new String(decrypted);
plainText = saltedPlainText.substring(0, saltedPlainText.length()-8);

System.out.println("**plainText: " + plainText);
