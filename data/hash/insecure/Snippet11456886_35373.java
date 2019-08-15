MessageDigest algorithm = MessageDigest.getInstance("MD5");
algorithm.reset();
algorithm.update(password.getBytes());
byte bytes[] = algorithm.digest();
StringBuffer sb = new StringBuffer();
for (byte b : bytes) {
    String hex = Integer.toHexString(0xff & b);
    if(hex.length() == 1)
        sb.append('0');
    sb.append(hex);
}
result = sb.toString();
