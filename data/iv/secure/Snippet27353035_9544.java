cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(java.util.Base64.Decoder.decode(IV.getBytes("UTF-8"))));
