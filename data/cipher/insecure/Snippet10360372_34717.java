 public static byte[] encrypt(byte[] key, byte[] data) throws Exception

        {

            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(data);
            return encrypted;
        }

        /**
         * DEcrypt byte array with given Key using AES Algorithm
         * Key can be generated using <Code>getKey()</Code>
         * @param key  Key that Is used for decrypting data
         * @param data  Data passed to decrypt
         * @return decrypted data
         * */

        public static byte[] decrypt1(byte[] key, byte[] encrypted) throws Exception
        {

            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] decrypted = cipher.doFinal(encrypted);
            return decrypted;
        }
        /**
         * get the Key for encryption this can be used for while decrypting and encrypting too.
         * */
        public static byte[] getKey() throws Exception
        {
            byte[] keyStart = EncrypteDecrypte.encryptionKey.getBytes();
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(keyStart);
            kgen.init(128, sr); // 192 and 256 bits may not be available
            SecretKey skey = kgen.generateKey();
            byte[] key = skey.getEncoded();

            return key;
        }
