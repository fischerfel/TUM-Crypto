String algorithm = "your algorithm value";//ex:PBKDF2WithHmacSHA512
String salt="randomString";
SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
PBEKeySpec spec = new PBEKeySpec(password, salt.getBytes(), 1000,256);
SecretKey key = skf.generateSecret(spec);
byte[] res = key.getEncoded();
String hashedPassword = res.toString();
