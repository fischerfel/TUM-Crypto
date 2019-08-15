Used org.apache.commons.codec.digest.DigestUtils; for calculating HEX

final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
final byte bin[] = messageDigest.digest((value.getBytes("UTF-8")));

final String hash = DigestUtils.sha256Hex(bin);
System.out.println("hex : " + hash);
