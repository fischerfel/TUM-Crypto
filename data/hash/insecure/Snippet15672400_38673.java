private String HexToSHA1(String ss) throws IllegalArgumentException {
    MessageDigest md = null;
    try { md = MessageDigest.getInstance("SHA-1"); }
    catch ( Exception e ) {}
    return byteArrayToHexString(md.digest(DatatypeConverter.parseHexBinary(ss)));
}
