   public static String md5(String input) throws UnsupportedEncodingException{
    String res = "";
    try {
        MessageDigest algorithm = MessageDigest.getInstance("MD5");
        algorithm.reset();
        algorithm.update(input.getBytes("UTF-8"));
        byte[] md5 = algorithm.digest();
        return md5.toString();
        }
     catch (NoSuchAlgorithmException ex) {}
    return res;
}
