InputStream in = new FileInputStream(filename);
MessageDigest md = MessageDigest.getInstance("MD5");

byte[] buf = new byte[8192];
int len;
while ((len = in.read(buf)) != -1) {
    md.update(buf, 0, len);
}
in.close();

byte[] bytes = md.digest();

StringBuilder sb = new StringBuilder(2 * bytes.length);
for (byte b : bytes) {
    sb.append("0123456789ABCDEF".charAt((b & 0xF0) >> 4));
    sb.append("0123456789ABCDEF".charAt((b & 0x0F)));
}
String hex = sb.toString();
