KeySpec ks = new DESKeySpec("MY KEY HERE".getBytes("UTF-8"));
SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(ks);
IvParameterSpec iv = new IvParameterSpec(Hex.decodeHex("1234567890ABCDEF".toCharArray()));
Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
cipher.init(Cipher.DECRYPT_MODE, key, iv);

byte[] decoded = cipher.doFinal(Base64.decodeBase64("MY ENCRYPTED STRING HERE"));

Log.e(TAG, "DECODED->" + new String(decoded, "UTF-8"));
