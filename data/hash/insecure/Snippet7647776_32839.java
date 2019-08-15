try {
    byte[] bytesOfchat_key = "lol".getBytes("UTF-8");
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] Digest = md.digest(bytesOfchat_key);
}
catch (NoSuchAlgorithmException e) {
    throw new RuntimeException("something impossible just happened", e);
}
catch (UnsupportedEncodingException e) {
    throw new RuntimeException("something impossible just happened", e);
}
