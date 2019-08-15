   /**
    * @param initialNoise device/app identifier. Use as many sources as possible to
    *    create this unique identifier.
    */
   public PixieObfuscator(String initialNoise) {
        try {
            // Hash up the initial noise into something smaller:
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            md.update(initialNoise.getBytes());
            byte[] hash = md.digest();

            // Allocate a buffer for our actual AES key:
            byte[] aesKEY = new byte[AES_KEY_LENGTH];   

            // Fill it with our lucky byte to take up whatever slack is not filled by hash:
            Arrays.fill(aesKEY, LUCKY_BYTE);

            // Copy in as much of the hash as we got (should be twenty bytes, take as much as we can):
            for (int i = 0; i < hash.length && i < aesKEY.length; i++)
                aesKEY[i] = hash[i];

            // Now make the damn AES key object:
              secret = new SecretKeySpec(aesKEY, "AES");
        }
        catch (GeneralSecurityException ex) {
            throw new RuntimeException("Exception in PixieObfuscator constructor, invalid environment");
        }
   }
