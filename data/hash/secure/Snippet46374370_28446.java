MessageDigest sha_c = MessageDigest.getInstance("SHA-256");
sha_c.update(r.toByteArray());
sha_c.update(d.toByteArray());
sha_c.update(C_ID.getBytes());
System.out.println(Arrays.toString(sha_c.digest()));

MessageDigest sha_b = MessageDigest.getInstance("SHA-256");
sha_b.update(r.toByteArray());
sha_b.update(d.toByteArray());
sha_b.update(R_ID.getBytes());
System.out.println(Arrays.toString(sha_b.digest()));
