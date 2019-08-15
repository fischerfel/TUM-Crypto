String plainText = "Hello, World! This is a Java/Javascript AES test.";
        try {
            byte[] rawKey = getRawKey("12345".getBytes());
            SecretKey key = new SecretKeySpec(rawKey, "AES");

            AlgorithmParameterSpec iv = new IvParameterSpec(
                    Base64.decodeBase64("5D9r9ZVzEYYgha93/aUK2w=="));

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            System.out.println(Base64.encodeBase64String(cipher
                    .doFinal(plainText.getBytes("UTF-8"))));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception in crypto...");
        }

public static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(seed);
        kgen.init(128, sr); // 192 and 256 bits may not be available
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        String s = new String(raw);
        System.out.println("raw key.." + raw);
        return raw;
    }
