try {
    byte[] encoded = MessageDigest.getInstance("SHA-1").digest("test".getBytes());
    byte[] hash = DatatypeConverter.parseHexBinary("a94a8fe5ccb19ba61c4c0873d391e987982fbbd3");

    System.out.println("Two byte array equals:\t\t" + Arrays.equals(hash, encoded));
} catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
}
