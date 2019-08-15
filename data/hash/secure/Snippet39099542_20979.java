public byte[] SHA256(String paramString) throws Exception {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    md.update(paramString.getBytes("UTF-8"));
    byte[] digest = md.digest();
    return digest;
}
