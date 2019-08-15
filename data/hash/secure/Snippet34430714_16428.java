MessageDigest md = MessageDigest.getInstance("SHA-256");
String text = "hello";
md.update(text.getBytes("UTF-8"));
byte[] digest = md.digest();
StringBuffer sb = new StringBuffer();
for (int i = 0; i < digest.length; i++) {
    sb.append(String.format("%02x", digest[i] & 0xFF))
}
System.out.println(sb.toString());
