 final MessageDigest md = MessageDigest.getInstance("SHA-1");
 byte[] digest = md.digest(dataBuffer.array());

 Cipher c2 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
 c2.init(Cipher.DECRYPT_MODE, cert.getPublicKey());
 byte[] decrypted2 = c2.doFinal(sigToCheck);
 final byte[] unpaddedSHA1 = Utils.unpadSHA1(decrypted2);

 System.out.println("signature verifies: " + Arrays.equals(digest, unpaddedSHA1));
