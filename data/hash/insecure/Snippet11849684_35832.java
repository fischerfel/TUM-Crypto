String hexMEID = "A0000000002329";

MessageDigest mDigest = MessageDigest.getInstance("SHA1");

byte[] b = new BigInteger(hexMEID,16).toByteArray();

// skip the first byte set by BigInteger and retain only 7 bytes (length of MEID)
byte[] result = mDigest.digest(Arrays.copyOfRange(b, 1, 8));

StringBuilder sb = new StringBuilder("80");

// need only the last 3 bytes
for (int i=17; i<20; i++) {
    sb.append(Integer.toHexString((result[i] & 0xff) | 0x100).substring(1));
}

String pESN = sb.toString();
System.out.println(pESN);
// -> 8051f1ab
