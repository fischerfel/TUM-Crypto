key = "abcd123456789kjd";
byteKey = key.getBytes();
MessageDigest sha = MessageDigest.getInstance("SHA-256");
byteKey = sha.digest(byteKey);
byteKey = Arrays.copyOf(byteKey, 32); // use only first 256 bit
secretKey = new SecretKeySpec(byteKey, "AES");
Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, secretKey);
