final String ALGORITHM = "AES";
final String TRANSFORMATION = "AES/CTR/PKCS5Padding";
final byte[] iv = trackIv.getBytes();
SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
cipher = Cipher.getInstance(TRANSFORMATION);
cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
