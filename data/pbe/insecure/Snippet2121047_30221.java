
String salt = "DC14DBE5F917C7D03C02CD5ADB88FA41";
String password = "25623F17-0027-3B82-BB4B-B7DD60DCDC9B";

char[] passwordChars = new char[password.length()];
password.getChars(0,password.length(), passwordChars, 0);

SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
KeySpec spec = new PBEKeySpec(passwordChars, salt.getBytes(), 2, 256);
SecretKey sKey = factory.generateSecret(spec);
byte[] raw = _sKey.getEncoded();

String toEncrypt = "The text to be encrypted.";

Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
cipher.init(Cipher.ENCRYPT_MODE, skey);

AlgorithmParameters params = cipher.getParameters();
byte[] initVector = params.getParameterSpec(IvParameterSpec.class).getIV();

byte[] encryptedBytes = cipher.doFinal(toEncrypt.getBytes());

