String password = null;
byte[] encoded = null;
try {
    MessageDigest md = MessageDigest.getInstance("MD5");
    encoded = md.digest(password.getBytes("UTF-8"));
} 
catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
    e.printStackTrace();
}
