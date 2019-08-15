public class Key {
   int primeCenterie = 20;
   BigInteger q;
   BigInteger p;
   BigInteger g;
   BigInteger y;
   BigInteger x;
   BigInteger k;
   Random rand = new Random();

Key() {}

public void generateKey() {
    q = new BigInteger(160, primeCenterie, rand);
    p = generateP(q, 512);
    g = generateG(p, q);
    do {
        x = new BigInteger(q.bitCount(), rand);
    } while (x.compareTo(BigInteger.ZERO) != 1 && x.compareTo(q) != -1);
    y = g.modPow(x, p);
}

private BigInteger generateP(BigInteger q, int l) {
    if (l % 64 != 0) {
        throw new IllegalArgumentException("L value is wrong");
    }
    BigInteger pTemp;
    BigInteger pTemp2;
    do {
        pTemp = new BigInteger(l, primeCenterie, rand);
        pTemp2 = pTemp.subtract(BigInteger.ONE);
        pTemp = pTemp.subtract(pTemp2.remainder(q));
    } while (!pTemp.isProbablePrime(primeCenterie) || pTemp.bitLength() != l);
    return pTemp;
}
private BigInteger generateG(BigInteger p, BigInteger q) {
    BigInteger aux = p.subtract(BigInteger.ONE);
    BigInteger pow = aux.divide(q);
    BigInteger gTemp;
    do {
        gTemp = new BigInteger(aux.bitLength(), rand);
    } while (gTemp.compareTo(aux) != -1 && gTemp.compareTo(BigInteger.ONE) != 1);
    return gTemp.modPow(pow, p);
}

public BigInteger generateK(BigInteger q) {
    BigInteger tempK;
    do {
        tempK = new BigInteger(q.bitLength(), rand);
    } while (tempK.compareTo(q) != -1 && tempK.compareTo(BigInteger.ZERO) != 1);
    return tempK;
}
public BigInteger generateR() {
    k = generateK(q);
    BigInteger r = g.modPow(k, p).mod(q);
    return r;
}

public BigInteger generateS(BigInteger r, byte[] data) {
    MessageDigest md;
    BigInteger s = BigInteger.ONE;
    try {
        md = MessageDigest.getInstance("SHA-1");
        md.update(data);
        BigInteger hash = new BigInteger(md.digest());
        s = (k.modInverse(q).multiply(hash.add(x.multiply(r)))).mod(q);
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(DSA.class.getName()).log(Level.SEVERE, null, ex);
    }
    return s;
}

boolean verify(byte[] data, BigInteger r, BigInteger s) {
    if (r.compareTo(BigInteger.ZERO) <= 0 || r.compareTo(q) >= 0) {
        return false;
    }
    if (s.compareTo(BigInteger.ZERO) <= 0 || s.compareTo(q) >= 0) {
        return false;
    }
    MessageDigest md;
    BigInteger v = BigInteger.ZERO;
    try {
        md = MessageDigest.getInstance("SHA-1");
        md.update(data);
        BigInteger hash = new BigInteger(md.digest());
        BigInteger w = s.modInverse(q);
        BigInteger u1 = hash.multiply(w).mod(q);
        BigInteger u2 = r.multiply(w).mod(q);
        v = ((g.modPow(u1, p).multiply(y.modPow(u2, p))).mod(p)).mod(q);
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(DSA.class.getName()).log(Level.SEVERE, null, ex);
    }
    return v.compareTo(r) == 0;
}}
