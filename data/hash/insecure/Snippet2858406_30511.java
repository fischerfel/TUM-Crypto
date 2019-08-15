public String encrypt (String password) {
    MessageDigest md = MessageDigest.getInstance("SHA-1");
    md.reset();     
    md.update(password.getBytes(Charset.forName("utf-8")),0,password.length());
    String res = md.digest().toString();
}
