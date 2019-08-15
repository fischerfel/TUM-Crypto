Random secureRandom = new SecureRandom();

byte[] ctr = new byte[16];
byte[] keyBytes = new byte[16];

secureRandom.nextBytes(ctr);
secureRandom.nextBytes(keyBytes);

IvParameterSpec ivSpec = new IvParameterSpec(ctr);
Key key = new SecretKeySpec(keyBytes, "AES");

byte[] plainText = new byte[8];

Cipher cipher = Cipher.getInstance("AES/CTR/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

byte[] cipherText = cipher.doFinal(plainText);
System.out.println(cipherText.length);
