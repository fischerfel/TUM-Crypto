private byte[] toBytes(char[] chars) {
    CharBuffer charBuffer = CharBuffer.wrap(chars);
    ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
    byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
            byteBuffer.position(), byteBuffer.limit());
    Arrays.fill(charBuffer.array(), '\u0000'); // clear sensitive data
    Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
    return bytes;
}

char[] passChars = passwordInputField.getPassword();
byte[] passBytes = toBytes(passChars);

MessageDigest md = MessageDigest.getInstance("MD5");
md.update(passBytes);
String hashedPass = new BigInteger(1, md.digest()).toString(16);

Arrays.fill(passChars, '\u0000'); // clear sensitive data
Arrays.fill(passBytes, (byte) 0); // clear sensitive data
