public static String encryptBlowFish(String cleartext, String key)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException
    {
        String encBlowFish = "";
        SecretKeySpec skeySpec = getGenerateKey(key);
        Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
        cipher.init(1, skeySpec);
        byte[] raw = cipher.doFinal(cleartext.getBytes("UTF8"));
        encBlowFish = new Base64Encoder().encode(raw);
        encBlowFish = URLEncoder.encode(encBlowFish, "UTF8");
        encBlowFish = specialChars(encBlowFish);
        return encBlowFish;
    }
