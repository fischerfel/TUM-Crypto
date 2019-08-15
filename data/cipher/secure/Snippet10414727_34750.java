MessageDigest digest = MessageDigest.getInstance("SHA-1");
digest.reset();
digest.update(serverCert.getTBSCertificate());
byte[] digestBytes = digest.digest();

Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.DECRYPT_MODE, caCert.getPublicKey());
byte[] cipherText = cipher.doFinal(serverCert.getSignature());
