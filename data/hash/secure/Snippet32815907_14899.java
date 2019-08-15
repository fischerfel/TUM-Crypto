/**
 * Produces a CFT proof as defined in Boudot paper. 
 * @param x The original message committed to in e.
 * @param r The random value in the commitment e of x.
 * @param e The value of the commitment.
 * @param b The bounding value such that x is in [0, b]
 * @param commit Object containing all the FO commitment parameters (e.g. g, h, n) and access to commit()
 * @return An array {C, D1, D2} that satisfies the CFT range-bounded commitment proof.
 */
public static BigInteger[] proofCFT(BigInteger x, BigInteger r, BigInteger e, BigInteger b, FujisakiOkamotoCommitment commit)
{
    SecureRandom rand = new SecureRandom();

    // Security parameters
    int t = 128; // Because SHA-256 is 256-bit
    int l = rand.nextInt(100);
    int s = rand.nextInt(100);

    // 2^(t+l+s)
    BigInteger tls2 = BigInteger.valueOf(2L).pow(t+l+s);

    // 2^(t+l)*b-1
    BigInteger b2tl_minus1 = BigInteger.valueOf(2L).pow(t+l).multiply(b).subtract(BigInteger.ONE);

    while(true)
    {
        BigInteger omega = getRandomInRange(BigInteger.ZERO, b2tl_minus1); // omega in [0, 2^(t+l)*b-1]
        BigInteger eta   = getRandomInRange( // eta in [-2^(t+l+s)*n+1, 2^(t+l+s)*n-1]
                commit.n().multiply(tls2).negate().add(BigInteger.ONE),
                commit.n().multiply(tls2).subtract(BigInteger.ONE));

        BigInteger w = commit.commit(omega, eta); // g^(omega)*h^(eta) mod n

        byte[] hash = new byte[0];
        try
        {
            hash = MessageDigest.getInstance("SHA-256").digest(w.toByteArray());
        }
        catch(NoSuchAlgorithmException nsae){} // Never occurs

        BigInteger bigC = new BigInteger(1, hash);
        BigInteger littleC = bigC.mod(BigInteger.valueOf(2L).pow(t)); // bigC mod 2^t

        BigInteger d1 = x.multiply(littleC).add(omega); // d1 = omega + xc
        BigInteger d2 = r.multiply(littleC).add(eta);   // d2 = eta + rc

        // Keep going until D1 in [c*b, b*2^(t+l)-1]
        BigInteger lowerBound = b.multiply(littleC);
        if(d1.compareTo(lowerBound) >= 0 && d1.compareTo(b2tl_minus1) <= 0) // Never succeeds??
            return new BigInteger[]{bigC, d1, d2};
    }
}
