final MessageDigest digest = MessageDigest.getInstance("SHA-1");
result = digest.digest(stringToHash.getBytes("UTF-8");

// Another way to make HEX, my previous post was only the method like your solution
StringBuilder sb = new StringBuilder();

for (byte b : result) // This is your byte[] result..
{
    sb.append(String.format("%02X", b));
}

String messageDigest = sb.toString();
