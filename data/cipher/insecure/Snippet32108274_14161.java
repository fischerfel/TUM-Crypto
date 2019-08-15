int slot = 0;
Provider provider = new au.com.safenet.crypto.provider.SAFENETProvider(slot);
Security.addProvider(provider);
final String PROVIDER = provider.getName(); // "SAFENET", "SAFENET.1", ...

KeyGenerator keyGen = KeyGenerator.getInstance("DESede", PROVIDER);
Key baseKey = keyGen.generateKey();

Cipher desCipher = Cipher.getInstance("DESede/CBC/PKCS5Padding", PROVIDER);
desCipher.init(Cipher.ENCRYPT_MODE, baseKey);

byte[] derived = desCipher.doFinal("diversification data".getBytes());
