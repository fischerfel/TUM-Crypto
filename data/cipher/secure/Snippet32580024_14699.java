private static final String INSTANCE = "AES/CBC/PKCS5Padding";
public static String decrypt(String sSrc, String sKey) throws Exception {
        try {
            // 判断Key是否正确  
            if (sKey == null) {
          //      LOGGER.error("Key should not be null");
                return null;
            }  
            if (sKey.length() != 16) {
                return null;
            }
            byte[] raw = sKey.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(INSTANCE);
            IvParameterSpec iv = new IvParameterSpec(sKey.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = Base64.decodeBase64(sSrc.getBytes());
            try {
                byte[] original = cipher.doFinal(encrypted1);
                return new String(original,"UTF-8");
            } catch (Exception e) {
                return null;
            }
        } catch (Exception ex) {
          //  LOGGER.error("decrypt fail", ex);
            return null;
        }
    }
