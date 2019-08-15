public static BigInteger initializeKeyPair(String word) throws Exception {
    byte[] publicKeyHash = MessageDigest.getInstance("SHA-256").digest(Crypto.getPublicKey(word));
    BigInteger bigInteger = new BigInteger(1,
        new byte[] {publicKeyHash[7], publicKeyHash[6], publicKeyHash[5],
            publicKeyHash[4], publicKeyHash[3], publicKeyHash[2],
            publicKeyHash[1], publicKeyHash[0]});
    return bigInteger;
}
