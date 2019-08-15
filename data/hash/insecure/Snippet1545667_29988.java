private String encodeAsMD5(String password) {
    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] bytes = md.digest(password.getBytes());
        return new String(Hex.encodeHex(bytes));
    } 
    catch(Exception e) {
        e.printStackTrace();
        return null;
    }
}
