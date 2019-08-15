private String md5(String md5Me) throws Exception {
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.reset();
    md.update(md5Me.getBytes("UTF-8"));

    return Base64.encodeBase64String(md.digest());
}
