try {
    MessageDigest msgDigest = MessageDigest.getInstance("MD5");
    byte digest[] = msgDigest.digest(username.getBytes());
    int secureHash = 1 + new BigInteger(digest).mod(BigInteger.valueOf(k)).intValue();

    System.out.println("Secure hash " + secureHash);
} catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
}
