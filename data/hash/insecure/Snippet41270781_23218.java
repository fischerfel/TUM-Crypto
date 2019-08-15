    private static String getMD5(String input) throws Exception {
       MessageDigest m = MessageDigest.getInstance("MD5");
       m.update(input.getBytes(), 0, input.length());
       return new BigInteger(1, m.digest()).toString(16);
   }
