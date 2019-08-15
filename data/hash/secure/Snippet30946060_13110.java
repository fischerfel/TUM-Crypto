byte[] digest = MessageDigest.getInstance("SHA-256").digest("password".getBytes("UTF-8"));

for (byte b : digest) {
    System.out.printf("%02x", b);
}
