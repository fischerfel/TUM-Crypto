public void encryptData() {
    String data = "Hello World";
    MessageDigest md = null;
    try {
        md = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    if (md == null) {
        return;
    }
    md.update(data.getBytes());
    String dataEncoded = Base64.encodeToString(md.digest(), 11);
    return dataEncoded; //print: sQqNsWTgdUEFt6mb5y4_5Q
}
