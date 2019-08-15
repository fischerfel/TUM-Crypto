
    public class Key {
        int primeCenterie = 20;
        BigInteger q;
        BigInteger p;
        BigInteger g;
        BigInteger y;
        BigInteger x;
        BigInteger k;
        Random rand = new Random();
        Key() {        }
        public void generateKey() {
            q = new BigInteger(160, primeCenterie, rand);
            p = generateP(q, 512);
            g = generateG(p, q);
            do {
                x = new BigInteger(q.bitCount(), rand);
            } while (x.compareTo(BigInteger.ZERO) == -1 || x.compareTo(g) == 1);
            y = g.modPow(x, p);
            System.out.println("p:" + p);
            System.out.println("q:" + q);
            System.out.println("g:" + g);
            System.out.println("private key  (x):" + x);
            System.out.println("public key  (y):" + y);
        }
       private BigInteger generateP(BigInteger q, int l) {
            if (l % 64 != 0) {
                throw new IllegalArgumentException(" zle l ");
            }
            BigInteger pTemp;
            BigInteger pTemp2;
            do {
                pTemp = new BigInteger(l, primeCenterie, rand);
                pTemp2 = pTemp.subtract(BigInteger.ONE);
                pTemp = pTemp.subtract(pTemp2.remainder(q));
            } while (!pTemp.isProbablePrime(primeCenterie));
            return pTemp;
        }
        private BigInteger generateG(BigInteger p, BigInteger q) {
            BigInteger aux = p.subtract(BigInteger.ONE);
            BigInteger pow = aux.divide(q);
            BigInteger g;
            do {
                g = new BigInteger(aux.bitLength(), rand);
            } while (g.compareTo(aux) == -1 && g.compareTo(BigInteger.ZERO) == 1);
            return g.modPow(pow, aux);
        }
        public BigInteger generateR() {
            BigInteger r = g.modPow(x, p).mod(q);
            System.out.println("r:" + r);
            return r;
        }
        public BigInteger generateS(BigInteger r, byte[] data) {
            MessageDigest md;
            BigInteger s = BigInteger.ONE;
            try {
                md = MessageDigest.getInstance("SHA-1");
                md.update(data);
                BigInteger hash = new BigInteger(md.digest());
                System.out.println("Hash:" + hash);
                s = (x.modInverse(q).multiply(hash.add(x.multiply(r)))).mod(q);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(DSA.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("s:" + s);
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
            System.out.println("v:" + v);
            System.out.println("r:" + r);
            return v.compareTo(r) == 0;
        }
    }
