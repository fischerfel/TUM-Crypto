private byte[] toBytes(char[] chars) {
    CharBuffer charBuffer = CharBuffer.wrap(chars);
    ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
    byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
            byteBuffer.position(), byteBuffer.limit());
    Arrays.fill(charBuffer.array(), '\u0000'); // clear sensitive data
    Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
    return bytes;
}

char[] stringChars = "String".toCharArray();
byte[] stringBytes = toBytes(stringChars);

MessageDigest md = MessageDigest.getInstance("MD5");
md.update(stringBytes);
String stringHash = new BigInteger(1, md.digest()).toString(16);

Arrays.fill(stringChars, '\u0000');
Arrays.fill(stringBytes, (byte) 0);
