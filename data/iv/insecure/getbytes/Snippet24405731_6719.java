SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
PBEKeySpec keyspec = new PBEKeySpec(password, salt, 1000, 256);
Key key = factory.generateSecret(keyspec);
SecretKeySpec secret = new SecretKeySpec(key.getEncoded(), "AES");
byte[] iv = "how_to_generate_in_java_as_in_c".getBytes();
AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, secret, ivSpec);
byte[] result = cipher.doFinal("asdfasdf".getBytes("UTF-8"));
