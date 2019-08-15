char[] passphrase; // Actual passphrase
PrivateKey privateKeyObj; // Actual Private Key
byte[] privateKeyBytes = privateKeyObj.getEncoded();

byte[] salt = new byte[8];
new SecureRandom().nextBytes(salt);

SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
KeySpec spec = new PBEKeySpec(passphrase, salt, 1024, 256);
SecretKey tmp = factory.generateSecret(spec);
SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, secret);
AlgorithmParameters params = cipher.getParameters();
byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();

byte[] ciphertext = cipher.doFinal(privateKeyBytes);

FileOutputStream outputFileStream = new FileOutputStream(outputFile);
outputFileStream.write(salt);
outputFileStream.write(iv);
outputFileStream.write(ciphertext);
outputFileStream.close();
