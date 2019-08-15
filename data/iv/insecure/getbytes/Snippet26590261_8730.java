String key = "123456"
final byte[] rgbIV = key.getBytes();
final IvParameterSpec iv = new IvParameterSpec(rgbIV);
byte[] ivBytes = iv.getIV();
StringBuffer sbuf = new StringBuffer();
for (byte b : ivBytes) {
    sbuf.append(String.format("%02x", (b & 0xFF)));
}
System.out.println("iv: " + sbuf);
