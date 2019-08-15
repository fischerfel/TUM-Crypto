String message = "Some Message";

MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");

messageDigest.update(message.getBytes("UTF-16BE"));
byte[] digest = messageDigest.digest();

StringBuffer digestInHex = new StringBuffer();

for (int i = 0, l = digest.length; i < l; i++) {
    // Preserve the bit representation when casting to integer.
    int intRep = digest[i] & 0xFF;
    // Add leading zero if value is less than 0x10.
    if (intRep < 0x10)  digestInHex.append('\u0030');
    // Convert value to hex.
    digestInHex.append(Integer.toHexString(intRep));
}

System.out.println(digestInHex.toString());
