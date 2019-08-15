public static SecretKeySpec getSecretKeySpec(String passphrase, String algorithm, int kgenbit)throws Exception
 {  
                   byte[] salt = {
                       (byte)0xA9, (byte)0x87, (byte)0xC8, (byte)0x32,
                       (byte)0x56, (byte)0xA5, (byte)0xE3, (byte)0xB2
                   };

                   // Iteration count
                   int iterationCount = 1024;

                   KeySpec keySpec = new PBEKeySpec(passphrase.toCharArray(), salt, iterationCount);

                   SecretKey secretKey = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

                   MessageDigest md = MessageDigest.getInstance("MD5");
                   md.update(secretKey.getEncoded());
                   md.update(salt);
                   for(int i = 1; i < iterationCount; i++)
                   md.update(md.digest());

                   byte[] keyBytes = md.digest();
                   skeyspec = new SecretKeySpec(keyBytes, algorithm);

                   return skeyspec;

             }

    public static byte[] encrypt(byte[] voicedata, int len)throws Exception{
          Cipher cipher = Cipher.getInstance(skeyspec.getAlgorithm());
          cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
          byte[] encrypted = cipher.doFinal(voicedata, 0, len);
          return encrypted;

    }
