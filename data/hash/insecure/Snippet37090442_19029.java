public static String md5(String input) throws NoSuchAlgorithmException{
    String result = input;
    if(input != null) {
        MessageDigest md = MessageDigest.getInstance("MD5"); 
        md.update(input.getBytes());
        BigInteger hash = new BigInteger(1, md.digest());
        result = hash.toString(16);
        while(result.length() < 32) {
            result = "0" + result;
        }
    }
    return result;
}

public static String sha256(String input) throws NoSuchAlgorithmException{
    String result = input;
    if(input != null) {
        MessageDigest md = MessageDigest.getInstance("MD5"); 
        md.update(input.getBytes());
        BigInteger hash = new BigInteger(1, md.digest());
        result = hash.toString(16);
        while(result.length() < 32) {
            result = "0" + result;
        }
    }
    return result;
}
