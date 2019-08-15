String s = "jake";
MessageDigest md = MessageDigest.getInstance("SHA-256");
md.update(s.getBytes(Charset.forName("UTF-8")));
byte[] hashed = md.digest();
String s2 = "";
for (byte b : hashed) {
    s2 += b;
}
System.out.println(s2);
