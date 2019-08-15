byte[] input = "test".getBytes();
String passphrase = "absnfjtyrufjdngjvhfgksdfrtifghkv";
int saltLength = 8; 

SecureRandom random = new SecureRandom();

//randomly generate salt
byte[] salt = new byte[saltLength];
random.nextBytes(salt);

// generating key from passphrase and salt
SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), salt, 1024, 256);
SecretKey key = factory.generateSecret(spec);
SecretKey kspec = new SecretKeySpec(key.getEncoded(), "AES");

// randomly generate IV
byte iv[] = new byte[16];
random.nextBytes(iv);
IvParameterSpec ips = new IvParameterSpec(iv);

Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
c.init(Cipher.ENCRYPT_MODE, kspec, ips);
byte[] encryptedData = c.doFinal(input);
System.out.println(new String(Base64.encodeBase64(encryptedData)));
