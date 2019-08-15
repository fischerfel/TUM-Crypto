public static synchronized String encrypt(String plainText) throws RuntimeException {
    MessageDigest md = null;

    try{
        md = MessageDigest.getInstance("SHA");
    }catch(NoSuchAlgorithmException e){
        throw new RuntimeException(e.getMessage());
    }

    try{
        md.update(plainText.getBytes("UTF-8"));
    }catch(UnsupportedEncodingException e){
        throw new RuntimeException(e.getMessage());
    }

    byte raw[] = md.digest();
    String hash = (new BASE64Encoder().encode(raw));
    return hash;
}
