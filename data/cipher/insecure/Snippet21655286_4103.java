// Java

byte[] rawKey = "deadbeefdeadbeef".getBytes("us-ascii");
SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
Cipher cip = Cipher.getInstance("AES/ECB/NoPadding");
cip.init(Cipher.DECRYPT_MODE, skeySpec);
byte[] plaintext = cip.doFinal(ciphertext, 0, ciphertext.length);
