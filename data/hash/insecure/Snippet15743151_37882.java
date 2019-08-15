MessageDigest md = MessageDigest.getInstance("MD5"); 
md.update('yourstring');
byte[] digest = md.digest();
StringBuffer sb = new StringBuffer();
for (byte b : digest) {
    sb.append(String.format("%02x", (0xFF & b)));
}
