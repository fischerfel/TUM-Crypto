MessageDigest digest = MessageDigest.getInstance("MD5");
int i = 0;
while (true)
{
    String raw = Integer.toString(i, Character.MAX_RADIX);
    byte[] md5 = digest.digest(raw.getBytes());
    String base64 = new BigInteger(1, md5).toString(16);
    System.out.println(raw + " = " + base64);
    i++;
}
