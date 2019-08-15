public static String buildPasswordDigest(String userName, String password, String nonce, String dateTime){
    MessageDigest sha1;
    String passwordDigest=null;
    try {
        sha1= MessageDigest.getInstance("SHA-1");
        byte[] hash = MessageDigest.getInstance("SHA-1").digest(password.getBytes("UTF-8"));
        sha1.update(nonce.getBytes("UTF-8"));
        sha1.update(dateTime.getBytes("UTF-8"));
        passwordDigest = new String(Base64.encode(sha1.digest(hash)));
        sha1.reset();
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return passwordDigest;
