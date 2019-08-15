class Main {
    public static void main(String[] a) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String output, input = "ml";
        MessageDigest md = MessageDigest.getInstance("md5");
        byte[] digest = md.digest(input.getBytes("UTF-8"));
        BigInteger bigInt = new BigInteger(1, digest);
        output = bigInt.toString(16);
        System.out.println(""+output);
    }
}
