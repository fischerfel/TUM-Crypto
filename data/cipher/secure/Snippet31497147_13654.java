private static final String truc = "f41a3ff27aab7d5c";


public static String encryptPass(String pass,String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(key.getBytes("UTF-8"));
        byte[] digest = md.digest();
        BCrypt bcrypt = new BCrypt();
        SecretKey keyL =  new SecretKeySpec(digest, "AES");

        Cipher AesCipher = Cipher.getInstance("AES/CTR/NoPadding");
        AesCipher.init(Cipher.ENCRYPT_MODE, keyL, new IvParameterSpec(truc.getBytes()));
        byte[] encVal = AesCipher.doFinal(pass.getBytes());
        pass = Base64.encodeToString(encVal, Base64.DEFAULT);

        Log.i("ADA", "encoded pass: " + pass);

        return pass;
    }

    public static String decryptPass(String encPass , String key) throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(key.getBytes("UTF-8"));
        byte[] digest = md.digest();

        SecretKey keyL =  new SecretKeySpec(digest, "AES");
        Cipher AesCipher = Cipher.getInstance("AES/CTR/NoPadding");
        AesCipher.init(Cipher.DECRYPT_MODE, keyL, new IvParameterSpec(truc.getBytes()));
        byte[] decodedValue = Base64.decode(encPass, Base64.DEFAULT);
        byte[] decValue = AesCipher.doFinal(decodedValue);
        String decryptedValue = new String(decValue);
        Log.i("ADA", "decrpyted pass: " + decryptedValue);

        return decryptedValue;
    }
