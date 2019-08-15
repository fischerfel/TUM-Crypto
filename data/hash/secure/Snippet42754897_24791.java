public String getSha256Hex(String text, String encoding){
    String shaHex = "";
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(text.getBytes(encoding));
        byte[] digest = md.digest();

        shaHex = DatatypeConverter.printHexBinary(digest);
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
        Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
    }
    return shaHex;
}
