public String createMd5(String password){
    String salt = "Random$SaltValue#WithSpecialCharacters12@$@4&#%^$*";
    String hash = md5(password + salt);
    return hash;
}

public static String md5(String input) {
    String md5 = null;
    if(null == input) return null;
    try {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(input.getBytes(), 0, input.length());
        md5 = new BigInteger(1, digest.digest()).toString(32);
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    return md5;
}
