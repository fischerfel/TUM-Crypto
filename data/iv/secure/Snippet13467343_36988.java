 public static void setENCRYPTION_IV(String ENCRYPTION_IV) {
        AESUtil.ENCRYPTION_IV  =   ENCRYPTION_IV;
    }

    public static void setENCRYPTION_KEY(String ENCRYPTION_KEY) {
        AESUtil.ENCRYPTION_KEY  =   ENCRYPTION_KEY;
    }



    public static String encrypt(String src) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, makeKey(), makeIv());
            return Base64.encodeBytes(cipher.doFinal(src.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String src) {
        String decrypted = "";
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, makeKey(), makeIv());
            decrypted = new String(cipher.doFinal(Base64.decode(src)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return decrypted;
    }

    static AlgorithmParameterSpec makeIv() {
        try {
            return new IvParameterSpec(ENCRYPTION_IV.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    static Key makeKey() {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] key = md.digest(ENCRYPTION_KEY.getBytes("UTF-8"));
            return new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
