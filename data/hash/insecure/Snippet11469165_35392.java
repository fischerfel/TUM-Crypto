md = MessageDigest.getInstance("SHA-1");
// Add values to the digest
String ipAddrHash = new String(md.digest());
