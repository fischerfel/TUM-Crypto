Cipher in = Cipher.getInstance("AES/CCM/NoPadding", "BC");
in.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(_nonce));
encoded = in.doFinal(payload);
