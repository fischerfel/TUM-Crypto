private static byte[] decryptData(byte[] data, String password,
            String paddingMode, String salt) throws Exception {
        if (data == null || data.length == 0)
            throw new IllegalArgumentException("data is empty");
        if (password == null || password == "")
            throw new IllegalArgumentException("password is empty");
        if (salt == null || salt == "")
            salt = ".";
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        byte[] saltBytes = salt.getBytes("UTF8");
        byte[] passBytes = password.getBytes("UTF8");

        PKCS5S1ParametersGenerator generator = new PasswordDeriveBytes(
                new SHA1Digest());
        generator.init(passBytes, saltBytes, 100);

        byte[] key = ((KeyParameter) generator.generateDerivedParameters(256))
                .getKey();
        passBytes = new byte[16];
        saltBytes = new byte[16];
        System.arraycopy(key, 0, passBytes, 0, 16);
        System.arraycopy(key, 16, saltBytes, 0, 16);

        Cipher cipher = Cipher.getInstance("AES/CBC/" + paddingMode, "BC");
        SecretKeySpec keySpec = new SecretKeySpec(passBytes, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec,
                new IvParameterSpec(saltBytes));

        byte[] original = cipher.doFinal(data);
        return original;
    }
