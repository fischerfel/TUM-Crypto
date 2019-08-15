// Encrypt as before

IvParameterSpec nullIv = new IvParameterSpec(new byte[16]);
cipher.init(Cipher.DECRYPT_MODE, key, nullIv);

cipher.update(realIv);
byte[] result = cipher.doFinal(encrypted);
// result.length == plain.length + 16
// just throw away the first block
