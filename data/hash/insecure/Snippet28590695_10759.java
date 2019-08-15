public abstract class PasswordHasher {

protected String algorithm;
protected MessageDigest md;

protected PasswordHasher(String algorithm) {
    try {
        md = MessageDigest.getInstance(algorithm);
    } catch (NoSuchAlgorithmException e) {
        System.err.println("Operacja nieprzewidziana");
        throw new Error();
    }
    this.algorithm = algorithm;
}

public String getAlgorithm() { return algorithm; }

public String hashText(String text) {
    StringBuilder sb = new StringBuilder();

    try {
        md.update(text.getBytes("UTF8"));
    } catch (Exception e) {
        System.err.println("Null pointer or UTF8 does not exists");
        return null;
    }

    byte byteData[] = md.digest();

    for(byte b : byteData)
        sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));

    return sb.toString();
} }

public class PasswordHasherSHA1 extends PasswordHasher {

public PasswordHasherSHA1() {
    super("SHA1");
} }
