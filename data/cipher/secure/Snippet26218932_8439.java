byte[] keyBytes = ... // password in bytes
javax.crypto.spec.SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
javax.crypto.Cipher cipher = Cipher.getInstance("PBEWITHSHA256AND128BITAES-CBC-BC");
cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
return new javax.crypto.CipherInputStream(in, cipher);
