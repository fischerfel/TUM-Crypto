String KEY_AES = "**************";
public String encrypt(String value) {
        try {
            byte[] key = KEY_AES.getBytes("UTF-8");
            byte[] ivs = KEY_AES.getBytes("UTF-8");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(ivs);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, paramSpec);
            return Base64.encodeToString(cipher.doFinal(value.getBytes("UTF-8")), Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
