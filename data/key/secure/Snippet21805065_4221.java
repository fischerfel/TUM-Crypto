public static SecretKey fromStringToAESkey(String s) {
        //256bit key need 32 byte
        byte[] rawKey = new byte[32];
        // if you don't specify the encoding you might get weird results
        byte[] keyBytes = new byte[0];
        try {
            keyBytes = s.getBytes("ASCII");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.arraycopy(keyBytes, 0, rawKey, 0, keyBytes.length);
        SecretKey key = new SecretKeySpec(rawKey, "AES");
        return key;
    }
