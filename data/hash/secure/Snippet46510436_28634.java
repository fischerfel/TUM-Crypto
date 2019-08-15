MessageDigest md = MessageDigest.getInstance("SHA-512");
byte[] digest = md.digest("Hello, world!".getBytes());
StringBuilder sb = new StringBuilder();
for (int i = 0; i < digest.length; i++) {
    sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
}
System.out.println(sb);
