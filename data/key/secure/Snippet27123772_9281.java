String key = enc_key;

// Create key and cipher
Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
Cipher cipher = Cipher.getInstance("AES");

// encrypt the text
cipher.init(Cipher.ENCRYPT_MODE, aesKey);
byte[] encrypted = cipher.doFinal(text.getBytes());
System.out.println(new String(encrypted));
