private Key generateKeyFromString(final String secKey) throws Exception {
    final byte[] keyVal = new BASE64Decoder().decodeBuffer(secKey);
    final Key key = new SecretKeySpec(keyVal, ALGORITHM);
    return key;
}
