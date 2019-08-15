import java.math.BigInteger;
import java.util.Random;
import java.security.*;

public class IPSec {

    private static final BigInteger one = new BigInteger("1");

    // private key (n,d)
    private BigInteger privateKey;
    // public key (n,e)
    private BigInteger publicKey = new BigInteger("3");
    // modulus n
    private BigInteger modulus;

    public IPSec() {
    }

    // PUBLIC KEY

    public BigInteger PublicKey() {
        return publicKey;
    }

    public BigInteger Modulus() {
        return modulus;
    }

    // KEY GENERATION

    public void KeyGen(int keyLength) {     
        BigInteger p = BigInteger.probablePrime((int)Math.ceil(keyLength / 2), new Random());
        BigInteger q = BigInteger.probablePrime((int)Math.ceil(keyLength / 2), new Random());

        while (!(p.subtract(one)).gcd(publicKey).equals(one))
            p = p.nextProbablePrime();

        while (!(q.subtract(one)).gcd(publicKey).equals(one))
            q = q.nextProbablePrime();

        BigInteger phi = (p.subtract(one)).multiply(q.subtract(one));       
        modulus = p.multiply(q);
        privateKey = publicKey.modInverse(phi);
    }

    // ENCRYPT

    public BigInteger Encrypt(BigInteger message) {
        return message.modPow(publicKey, modulus);
    }

    public static BigInteger Encrypt(BigInteger message, BigInteger publicKey, BigInteger modulus) {
        return message.modPow(publicKey, modulus);
    }

    // DECRYPT

    public BigInteger Decrypt(BigInteger message) {
        return message.modPow(privateKey, modulus);
    }

    // SIGNATURE GENERATION 

    // Generate RSA-signatures for a message
    public BigInteger GenerateRSASignature(BigInteger message) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            return Decrypt(new BigInteger(1, digest.digest(message.toByteArray())).mod(Modulus()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return message;
    }

    // Verify RSA-signatures for a message
    public boolean VerifyRSASignature(BigInteger message, BigInteger signature) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return (new BigInteger(1, digest.digest(message.toByteArray())).mod(Modulus())).equals(Encrypt(signature));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return false;
    }

    public boolean VerifyRSASignature(BigInteger message, BigInteger signature, 
            BigInteger publicKey, BigInteger modulus) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return (new BigInteger(1, digest.digest(message.toByteArray())).mod(Modulus())).equals(Encrypt(signature, publicKey, modulus));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return false;
    }

    public static void main(String[] args) {
        Testing();
    }

    // MISC

    public void printKeys() {
        String s = "";
        s += "public  = " + publicKey  + "\n";
        s += "private = " + privateKey + "\n";
        s += "modulus = " + modulus;
        System.out.println(s);
    }

    public static void Testing() {
        IPSec gen = new IPSec();
        gen.KeyGen(128);
        BigInteger message = new BigInteger("329");
        System.out.println("Verify: " + gen.VerifyRSASignature(message, gen.GenerateRSASignature(message)));
    }
}
