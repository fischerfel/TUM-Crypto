//IV. 
byte[] bytes = new byte[16];
SecureRandom random = new SecureRandom();
random.nextBytes(bytes);

Map<String, byte[]> aes = new HashMap<String, byte[]>();

aes.put("IV", ConversionUtil.toHex(bytes, 8).getBytes());

KeyGenerator keyGen = KeyGenerator.getInstance("AES");

keyGen.init(256);
Key encryptionKey = keyGen.generateKey();

Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); 

cipher.init(Cipher.ENCRYPT_MODE, encryptionKey, new IvParameterSpec(ConversionUtil.toHex(bytes, 8).getBytes()));

aes.put("key", cipher.doFinal(encryptionKey.getEncoded()));
