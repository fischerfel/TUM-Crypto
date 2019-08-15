private static Key generateKey() throws Exception {
final byte[] decodedKey = new BASE64Decoder().decodeBuffer(KEY_VALUE);
final Key key = new SecretKeySpec(decodedKey, _AES);
return key;
}
