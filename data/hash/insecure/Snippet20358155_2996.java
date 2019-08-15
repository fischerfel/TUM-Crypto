public String hash(String value) {
    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(value.getBytes("UTF-8"));
        return md.toString();
    } catch (NoSuchAlgorithmException e) {
        return null;
    } catch (UnsupportedEncodingException e) {
        return null;
    }
}
