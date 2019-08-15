String password = "passPhrase";
String salt = "15charRandomSalt";
int iterations = 100;

/* Derive the key, given password and salt. */
SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(Charset.forName("UTF8")), iterations, 256);
SecretKey tmp = factory.generateSecret(spec);
SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

/* Encrypt the message. */
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, secret);
AlgorithmParameters params = cipher.getParameters();
byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
byte[] cipherText = cipher.doFinal(toBeEncrypted.getBytes("UTF-8"));
encryptedData = Base64.getEncoder().encodeToString(cipherText);
encryptedData += Base64.getEncoder().encodeToString(iv);
