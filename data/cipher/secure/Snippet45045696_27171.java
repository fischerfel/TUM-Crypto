String Test = "Lorem ipsum dolor sit amet, ...";
String password = "test";

byte[] salt = new String("12345678").getBytes("Utf8");
int iterationCount = 2048;
int keyStrength = 256;

SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterationCount, keyStrength);
SecretKey tmp = factory.generateSecret(spec);

Log.d("encryptString Key: ", new String(Base64.encodeBase64(tmp.getEncoded())));

Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
c.init(Cipher.ENCRYPT_MODE, tmp);
byte[] decrypted = c.doFinal(Test.getBytes());
decrypted = Base64.encodeBase64(decrypted);
byte[] iv = c.getIV();

Log.d("encryptString: ", new String(decrypted));
Log.d("encryptString iv:", new String(Base64.encodeBase64(iv)));
