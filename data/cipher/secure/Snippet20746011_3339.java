javax.crypto.Cipher out = Cipher.getInstance("AES/CCM/NoPadding", "BC");
out.init(Cipher.DECRYPT_MODE, key, new javax.crypto.spec.IvParameterSpec.IvParameterSpec(nonce));
byte[] decrypted = out.doFinal(encrypted);
