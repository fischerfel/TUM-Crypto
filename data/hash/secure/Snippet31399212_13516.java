public boolean testValidity(String s) {
    try {
        MessageDigest md = MessageDigest.getInstance(name());
        byte[] hashDigest = md.digest(s.getBytes("UTF-8"));
        String hash = String.format("%032x", new BigInteger(md.digest(s.getBytes("UTF-8"))));
        System.out.println(hash);
        return getCompare().equalsIgnoreCase(hash);
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException | NullPointerException e) {
        e.printStackTrace();
        return false;
    }
}
