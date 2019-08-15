for (byte b : MessageDigest.getInstance("SHA-256")
        .digest(Long.toString((Long.MAX_VALUE >> 0x1a ^ 0xcafebabeL)).getBytes()))
    System.out.printf("%02x", b);
