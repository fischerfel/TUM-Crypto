static RSAPrivateKey decrypt(String keyDataStr, String ivHex, String password)
            throws GeneralSecurityException, UnsupportedEncodingException
          {
            byte[] pw = password.getBytes(StandardCharsets.UTF_8);
            byte[] iv = h2b(ivHex);
            SecretKey secret = opensslKDF(pw, iv);
            Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
            byte [] keyBytes=Base64.decode(keyDataStr.getBytes("UTF-8"));
            byte[] pkcs1 = cipher.doFinal(keyBytes);
            /* See note for definition of "decodeRSAPrivatePKCS1" */
            RSAPrivateCrtKeySpec spec = decodeRSAPrivatePKCS1(pkcs1);
            KeyFactory rsa = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) rsa.generatePrivate(spec);
          }

          private static SecretKey opensslKDF(byte[] pw, byte[] iv)
            throws NoSuchAlgorithmException
          {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(pw);
            md5.update(iv);
            byte[] d0 = md5.digest();
            md5.update(d0);
            md5.update(pw);
            md5.update(iv);
            byte[] d1 = md5.digest();
            byte[] key = new byte[24];
            System.arraycopy(d0, 0, key, 0, 16);
            System.arraycopy(d1, 0, key, 16, 8);
            return new SecretKeySpec(key, "DESede");
          }

          private static byte[] h2b(CharSequence s)
          {
            int len = s.length();
            byte[] b = new byte[len / 2];
            for (int src = 0, dst = 0; src < len; ++dst) {
              int hi = Character.digit(s.charAt(src++), 16);
              int lo = Character.digit(s.charAt(src++), 16);
              b[dst] = (byte) (hi << 4 | lo);
            }
            return b;
          }
          static RSAPrivateCrtKeySpec decodeRSAPrivatePKCS1(byte[] encoded)
          {
            ByteBuffer input = ByteBuffer.wrap(encoded);
            if (der(input, 0x30) != input.remaining())
              throw new IllegalArgumentException("Excess data");
            if (!BigInteger.ZERO.equals(derint(input)))
              throw new IllegalArgumentException("Unsupported version");
            BigInteger n = derint(input);
            BigInteger e = derint(input);
            BigInteger d = derint(input);
            BigInteger p = derint(input);
            BigInteger q = derint(input);
            BigInteger ep = derint(input);
            BigInteger eq = derint(input);
            BigInteger c = derint(input);
            return new RSAPrivateCrtKeySpec(n, e, d, p, q, ep, eq, c);
          }

          private static BigInteger derint(ByteBuffer input)
          {
            byte[] value = new byte[der(input, 0x02)];
            input.get(value);
            return new BigInteger(+1, value);
          }


          private static int der(ByteBuffer input, int exp)
          {
            int tag = input.get() & 0xFF;
            if (tag != exp)
              throw new IllegalArgumentException("Unexpected tag");
            int n = input.get() & 0xFF;
            if (n < 128)
              return n;
            n &= 0x7F;
            if ((n < 1) || (n > 2))
              throw new IllegalArgumentException("Invalid length");
            int len = 0;
            while (n-- > 0) {
              len <<= 8;
              len |= input.get() & 0xFF;
            }
            return len;
          }
