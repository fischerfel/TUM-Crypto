final MessageDigest md = MessageDigest.getInstance("SHA1");

byte[] digest = md.digest(dataBuffer.array());

// RSA decrypt

Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

cipher.init(Cipher.DECRYPT_MODE, cert);

byte[] decrypted = cipher.doFinal(sigToCheck);    



System.out.println("signature verifies: " + Arrays.equals(digest, decrypted));
