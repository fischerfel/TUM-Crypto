public AesEncrypt(String key) {
        SecretKeySpec skey = new SecretKeySpec(getMD5(key), "AES");
        setupCrypto(skey);
    }

    private void setupCrypto(SecretKey key) {
        byte[] iv = {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        System.out.println("key:  " + key);
        System.out.println("iv:  " + iv);

        // String iv = "fedcba9876543210";
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
        System.out.println("paramspec:  " + paramSpec);
        try {
            this.ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            this.dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            System.out.println("this.ecipher:  " + this.ecipher);
            this.ecipher.init(1, key, paramSpec);
            this.dcipher.init(2, key, paramSpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
