public static void main(String[] args) throws NoSuchAlgorithmException {
        String s = "oshai";
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(s.getBytes(),0,s.length());
        String md5 = new BigInteger(1,m.digest()).toString(16);
        System.out.println(md5.length());
}
