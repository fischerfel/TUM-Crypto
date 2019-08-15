// myDoc is my Document object;
byte[] docBytes = serialize(myDoc);
byte[] key = ("sample-16chr-key").getBytes("UTF-8");
IvParameterSpec iv = new IvParameterSpec(key);

Cipher c = Cipher.getInstance("AES");
SecretKeySpec k = new SecretKeySpec(key, "AES");
c.init(Cipher.DECRYPT_MODE, k, iv);

// IllegalBlockSizeException Occurred
byte[] decryptedDocBytes = c.doFinal(docBytes);

Document decryptedDoc = (Document)deserialize(decryptedDocBytes);
