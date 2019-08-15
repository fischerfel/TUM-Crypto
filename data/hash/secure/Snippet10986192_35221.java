package ecc;
import NISTCurves.ECDomainParameters;
import NISTCurves.P192;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Gere
 */
public class ECDSA {

    private BigInteger r, s;
    ECDomainParameters param;
    private PrivateKey prvKey;
    private PublicKey pubKey;
    BigInteger zero = BigInteger.ZERO;
    private BigInteger one = BigInteger.ONE;
    private MessageDigest sha;

    public ECDSA() {
        try {
            sha = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
    }

    public void initSign(PrivateKey prvKey) {
        this.prvKey = prvKey;
        param = prvKey.getParam();
    }

    public void initVerify(PublicKey pubKey) {
        this.pubKey = pubKey;
        param = pubKey.getParam();
    }

    public void update(byte[] byteMsg) {
        sha.update(byteMsg);
    }

    public byte[] sign() throws FileNotFoundException, IOException {

        BigInteger c = new BigInteger(
                                   param.getP().bitLength() + 64,  Rand.sr);
        BigInteger k = c.mod(param.getOrder().subtract(one)).add(one);
        while (!(k.gcd(param.getOrder()).compareTo(one) == 0)) {
            c = new BigInteger(param.getP().bitLength() + 64, Rand.sr);
            k = c.mod(param.getOrder().subtract(one)).add(one);
        }
        BigInteger kinv = k.modInverse(param.getOrder());
        ECPointArthimetic p = param.getGenerator().multiply(k);
        if (p.getX().equals(zero)) {
            return sign();
        }
        BigInteger hash = new BigInteger(sha.digest());
        BigInteger r = p.getX().mod(param.getOrder());

        BigInteger s = (kinv.multiply((hash.add((prvKey.getPrivateKey()
                                  .multiply(r)))))).mod(param.getOrder());
        if (s.compareTo(zero) == 0) {
            return sign();
        }

        System.out.println("r at sign: " + r);
        System.out.println("s at sign: " + s);

        byte[] rArr = toUnsignedByteArray(r);
        byte[] sArr = toUnsignedByteArray(s);
        int nLength = (param.getOrder().bitLength() + 7) / 8;
        byte[] res = new byte[2 * nLength];
        System.arraycopy(rArr, 0, res, nLength - rArr.length, rArr.length);

        System.arraycopy(sArr, 0, res, 2 * nLength - sArr.length,
                      sArr.length);
        return res;
    }

    public boolean verify(byte[] res) {

        int nLength = (param.getOrder().bitLength() + 7) / 8;

        byte[] rArr = new byte[nLength];
        System.arraycopy(res, 0, rArr, 0, nLength);
        r = new BigInteger(rArr);

        byte[] sArr = new byte[nLength];
        System.arraycopy(res, nLength, sArr, 0, nLength);
        s = new BigInteger(sArr);
        System.out.println("r at verify: " + r);
        System.out.println("s at verify: " + s);
        BigInteger w, u1, u2, v;
        // r in the range [1,n-1]
        if (r.compareTo(one) < 0 || r.compareTo(param.getOrder()) >= 0) {
            return false;
        }

        // s in the range [1,n-1]
        if (s.compareTo(one) < 0 || s.compareTo(param.getOrder()) >= 0) {
            return false;
        }
        w = s.modInverse(param.getOrder());

        BigInteger hash = new BigInteger(sha.digest());
        u1 = hash.multiply(w);
        u2 = r.multiply(w);

        ECPointArthimetic G = param.getGenerator();
        ECPointArthimetic Q = pubKey.getPublicKey();

        // u1G + u2Q

        ECPointArthimetic temp = G.implShamirsTrick(u1, Q, u2);
        v = temp.getX();
        v = v.mod(param.getOrder());

        return v.equals(r);
    }

    byte[] toUnsignedByteArray(BigInteger bi) {
        byte[] ba = bi.toByteArray();
        if (ba[0] != 0) {
            return ba;
        } else {
            byte[] ba2 = new byte[ba.length - 1];
            System.arraycopy(ba, 1, ba2, 0, ba.length - 1);
            return ba2;
        }
    }

    public static void main(String[] args) {
        byte[] msg = "Hello".getBytes();
        byte[] sig = null;
        ECDomainParameters param = new P192();        
        PrivateKey prvObj = new PrivateKey(param);
        PublicKey pubObj = new PublicKey(prvObj);
        ECDSA ecdsa = new ECDSA();
        ecdsa.initSign(prvObj);
        ecdsa.update(msg);
        try {
            sig = ecdsa.sign();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        ecdsa.initVerify(pubObj);
        ecdsa.update(msg);
        if (ecdsa.verify(sig)) {
            System.out.println("valid");
        } else {
            System.out.println("invalid");
        }
    }
}
