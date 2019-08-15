Cipher bobCipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
byte[] iv = generateNonce(nonce_bytes12, 0);
bobCipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));

// adding firefox padding:
bobCipher.update(new byte[1]);

byte[] cleartext = {...};
