long t0 = System.currentTimeMillis();
byte[] bytes = new byte[100];
MessageDigest md = MessageDigest.getInstance("MD5");
for(int i = 0; i < 1000000; i++) {
    //MessageDigest md = MessageDigest.getInstance("MD5");
    md.reset();
    md.update(bytes);
    md.digest();
}
System.out.println(System.currentTimeMillis() - t0);
