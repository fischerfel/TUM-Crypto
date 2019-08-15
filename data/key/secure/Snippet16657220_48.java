 public static String decrypt(String seed, String encrypted) throws Exception {
            byte[] rawKey = getRawKey(seed.getBytes());
            System.out.println(rawKey);
          //  byte[] enc = toByte(encrypted);
            byte[] enc = Base64.decode(encrypted);
            byte[] result = decrypt(rawKey, enc);
            return new String(result);
        }

  private static byte[] getRawKey(byte[] seed) throws Exception {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(seed);
            kgen.init(128, sr); // 192 and 256 bits may not be available
            SecretKey skey = kgen.generateKey();
            byte[] raw = skey.getEncoded();
            return raw;
        }


  private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] decrypted = cipher.doFinal(encrypted);
            return decrypted;
        }
