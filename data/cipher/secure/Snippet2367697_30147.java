AlgorithmParameterSpec paramSpec = new IvParameterSpec(initv);
encipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
mac = Mac.getInstance("HmacSHA512");
encipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
mac.init(key);
buf = new byte[encipher.getOutputSize(blockSize)];
