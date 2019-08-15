password="testKey";
final MessageDigest md = MessageDigest.getInstance("SHA1");
ByteArrayOutputStream pwsalt = new ByteArrayOutputStream();
pwsalt.write(password.getBytes("UTF-8"));
byte[] unhashedBytes = pwsalt.toByteArray();
byte[] digestVonPassword = md.digest(unhashedBytes);
System.out.println(bytesToHex(digestVonPassword));
