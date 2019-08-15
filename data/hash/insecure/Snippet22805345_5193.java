    MessageDigest md = MessageDigest.getInstance("MD5");  
    md.update("test".getBytes());
    byte[] a = md.digest();     // 16 bytes for MD5
    ByteBuffer bb = ByteBuffer.wrap(a);
    long l1 = bb.getLong();
    long l2 = bb.getLong();
    UUID uuid = new UUID(l1, l2);
    System.out.println(uuid);
