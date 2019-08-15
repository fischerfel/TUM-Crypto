   passphrase = passphrase + STATIC_KEY;
   byte[] key = passphrase.getBytes("UTF-8");

   MessageDigest sha = MessageDigest.getInstance("SHA-1");
   key = sha.digest(key);
   key = Arrays.copyOf(key, 16);
   SecretKey secretKey = new SecretKeySpec(key, "AES");

   Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
   IvParameterSpec initialisationVector = new IvParameterSpec(
           new byte[16]);
   cipher.init(Cipher.ENCRYPT_MODE, secretKey, initialisationVector);

   byte[] encryptedData = cipher.doFinal(plainText.getBytes("UTF-8"));

   return SimpleCrypto.toHex(encryptedData);
