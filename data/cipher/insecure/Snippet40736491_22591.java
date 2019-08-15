public String encrypt(String message) throws Exception {

        final MessageDigest md = MessageDigest.getInstance("md5");
        final byte[] digestOfPassword = md.digest("1234567890123".getBytes());
        final byte[] keyBytes = Arrays.copyOf(Arrays.copyOf(digestOfPassword, 12), 24);

        DESedeKeySpec spec = new DESedeKeySpec(keyBytes);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey theKey = keyFactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, theKey);
        byte[] plaintext = message.getBytes();
        byte[] encrypted = cipher.doFinal(plaintext);

        final String encodedCipherText = Base64.getEncoder().encodeToString(encrypted);

        return encodedCipherText;
    }
