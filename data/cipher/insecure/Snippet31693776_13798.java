    private static String algorithm = "RC4";

            public static byte[] encryptRC4(String toEncrypt, String key) throws Exception {
              // create a binary key from the argument key (seed)
              SecureRandom sr = new SecureRandom(key.getBytes("ISO-8859-1"));
              KeyGenerator kg = KeyGenerator.getInstance(algorithm);
              kg.init(sr);
              SecretKey sk = kg.generateKey();

              // create an instance of cipher
              Cipher cipher = Cipher.getInstance(algorithm);

              // initialize the 

cipher with the key
          cipher.init(Cipher.ENCRYPT_MODE, sk);

          // enctypt!
          byte[] encrypted = cipher.doFinal(toEncrypt.getBytes("ISO-8859-1"));

          return encrypted;
        }

        public static String decryptRC4(byte[] toDecrypt, String key, int length) throws Exception {
          // create a binary key from the argument key (seed)
          SecureRandom sr = new SecureRandom(key.getBytes("ISO-8859-1"));
          KeyGenerator kg = KeyGenerator.getInstance(algorithm);
          kg.init(sr);
          SecretKey sk = kg.generateKey();

          // do the decryption with that key
          Cipher cipher = Cipher.getInstance(algorithm);
          cipher.init(Cipher.DECRYPT_MODE, sk);
          byte[] decrypted = cipher.doFinal(toDecrypt, 0, length);


          return new String(decrypted, "ISO-8859-1");
       }
