@Test
public void testSHA256LengthConsistent() {
    MessageDigest sha256 = null;
    try {
        sha256 = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
        Assert.fail("NoSuchAlgorithmException. Can't construct the MessageDigest.");
    }
    BigInteger[] tests = {new BigInteger("15902493"), new BigInteger("5189087324092341824"), new BigInteger("7153293421609183203421127438153268")};
    for(BigInteger testNum : tests) {
        byte[] hash = sha256.digest(testNum.toByteArray());
        Assert.assertEquals(32, hash.length); //256 bits is 32 bytes
        BigInteger hashedInt = new BigInteger(1, hash);
        Assert.assertEquals(256, hashedInt.bitLength());
    }
}
