package entry;

public class Encrypt {
    SecretKey skey;
    SecretKeySpec skey_spec;
    byte[] iv;
    IvParameterSpec iv_spec;

    KeyPair rsaKey;

    Random random;

    public Encrypt() {
        random = new Random();
    }

    public void initAES() {
        System.out.println("Initializing AES Keys...");
        KeyGenerator kgen = null;

        try {
            kgen = KeyGenerator.getInstance("AES");
        } catch(NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        }

        kgen.init(256);

        // Generate the secret key specs.
        skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();

        skey_spec = new SecretKeySpec(raw, "AES");

        iv = new byte[16];
        random.nextBytes(iv);
        iv_spec = new IvParameterSpec(iv);
    }
}
