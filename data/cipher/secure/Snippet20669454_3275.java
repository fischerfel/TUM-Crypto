PublicKey publicKey = cert.getPublicKey();
cipher = Cipher.getInstance("RSA", "BC");        
cipher.init(Cipher.ENCRYPT_MODE, publicKey);
