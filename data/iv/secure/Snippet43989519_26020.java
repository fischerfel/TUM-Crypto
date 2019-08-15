SecureRandom random = new SecureRandom();
byte[] iv = new byte[ivSizeBytes];
random.nextBytes(iv);
new IvParameterSpec(iv);
