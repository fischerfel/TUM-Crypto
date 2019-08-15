MessageDigest md = MessageDigest.getInstance(algorithm);
md.update(original.getBytes());
byte[] digest = md.digest();
StringBuffer sb = new StringBuffer();
for (byte b : digest) {
    sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
}
return sb.toString();
