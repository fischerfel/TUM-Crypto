public static String MD5_Hash(String s) {
        MessageDigest m = null;

        try {
                m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
        }

        //m.update(s.getBytes(),0,s.length());
        byte [] data = hexStringToByteArray(s);
        m.update(data, 0, data.length);
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
}
