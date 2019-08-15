private String getSecWebSocketAccept(String secKey) 
    throws UnsupportedEncodingException, NoSuchAlgorithmException {
    String guid = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
    secKey += guid;
    MessageDigest md = MessageDigest.getInstance("SHA-1");
    md.update(secKey.getBytes("ISO-8859-1"), 0, secKey.length());
    byte[] shalHash = md.digest();
    BASE64Encoder encoder = new BASE64Encoder();
    return encoder.encode(shalHash);
}
