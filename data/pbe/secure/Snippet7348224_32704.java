// Read in the salt.
byte[] salt = new byte[8];
ByteArrayInputStream bais = new ByteArrayInputStream(ciphertext);
bais.read(salt,0,8);

// The remaining bytes are the actual ciphertext.
byte[] remainingCiphertext = new byte[ciphertext.length-8];
bais.read(remainingCiphertext,0,ciphertext.length-8);

// Create a PBE cipher to decrypt the byte array.
PBEKeySpec keySpec = new PBEKeySpec(password);
SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithSHAAndTwofish-CBC");
SecretKey key = keyFactory.generateSecret(keySpec);
PBEParameterSpec paramSpec = new PBEParameterSpec(salt, ITERATIONS);
Cipher cipher = Cipher.getInstance("PBEWithSHAAndTwofish-CBC");

// Perform the actual decryption.
cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
return cipher.doFinal(remainingCiphertext);
  }
}
