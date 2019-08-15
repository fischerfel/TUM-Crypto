private static String applyHashToKey(String key){
        byte[] hashkey = null;
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            hashkey = md.digest(new BASE64Decoder().decodeBuffer(key));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String result = new BASE64Encoder().encodeBuffer(hashkey);
        return result;
    }
