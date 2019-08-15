MessageDigest digest = MessageDigest.getInstance("SHA-1");
digest.update((byte) 0xFF);
byte[] result = digest.digest();

StringBuilder buffer = new StringBuilder();
for (byte each : result)
    buffer.append(String.format("%02x", 0xFF & each));
System.out.println(buffer.toString());
