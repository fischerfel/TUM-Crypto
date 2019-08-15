public String getHash(String password, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    md.reset();
    md.update(salt);
    return new String(md.digest(password.getBytes("UTF-8")));
}
