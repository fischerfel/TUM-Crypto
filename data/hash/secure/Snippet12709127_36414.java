MessageDigest md = MessageDigest.getInstance("SHA-256");
byte[] digest = md.digest("testString".getBytes());
StringBuilder sb = new StringBuuilder();
for (byte b : digest) {
    sb.append(Integer.toHexString(b & 0xff));
}
String base64 = Base64.encodeBase64(sb.toString());
