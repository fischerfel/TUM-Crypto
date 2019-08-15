public static byte[] encryptLB(byte[] key, byte[] iv, byte[] unencrypted)
               throws NoSuchAlgorithmException, ... {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(key);
            byte[] hash = digest.digest(); //build the hash (128 bit)

              Cipher cipher = Cipher.getInstance("RC2/CBC/PKCS5Padding");
              cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(hash, "RC2"));
              byte[] unByte = unencrypted;
              byte[] encrypted = cipher.doFinal(unencrypted);
              return encrypted;
             }
