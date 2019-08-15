private String getSecWebSocketAccept(String secKey) 
    throws UnsupportedEncodingException, NoSuchAlgorithmException {
    String guid = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
    secKey += guid;
    MessageDigest md = MessageDigest.getInstance("SHA-1");
    md.update(secKey.getBytes("ISO-8859-1"), 0, secKey.length());
    byte[] shalHash = md.digest();
    org.apache.commons.codec.binary.Base64.encodeBase64 encoder = new org.apache.commons.codec.binary.Base64.encodeBase64();
    return encoder.encode(shalHash);
}
