Cipher cipher = Cipher.getInstance("AES/CCM/NoPadding", "BC");
SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
IvParameterSpec ivParameterSpec = new IvParameterSpec(nonce);
cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
byte[] encrypted = cipher.doFinal(testInput);

// The first 16 bytes print out equivalently with the C-language AES/CCM
