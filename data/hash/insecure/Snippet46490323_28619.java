 static String Hash(String input) throws Exception {

    MessageDigest mDigest = MessageDigest.getInstance("SHA1");
    byte[] result = mDigest.digest(input.getBytes());
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < result.length; i++) {
       sb.append((result[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
    }
    return base64_encode(sb.toString());

}
