   static public synchronized String encryptAesData(Context pContext, String pData, byte[] pKey, byte[] pIv) {
        Log.d("", "key size <" + pKey.length + "> iv size <" + pIv.length + ">"); //key size <32> iv size <16>

        AlgorithmParameterSpec paramSpec = new IvParameterSpec(pIv);
        SecretKeySpec key = new SecretKeySpec(pKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        byte[] encrypted = cipher.doFinal(pData.getBytes());
        return Base64.encodeToString(encrypted, Base64.DEFAULT);
