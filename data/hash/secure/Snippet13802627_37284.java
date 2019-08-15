MessageDigest md = MessageDigest.getInstance("SHA-256");
md.update(salt);
md.update(payload1);  // part 1 of payload
md.update(payload2);  // part 2 of payload
md.update(serialNumber);  // part 3 of payload
md.reset();
byte[] sig = md.digest();
for (int i=0; i<1000; i++) {
  md.reset();
  sig = md.digest(sig);
}
