@Component
public class MedusaDigests {
    private final String MD5 = "MD5";
    private MessageDigest mda = null;

    public MedusaDigests() {
        try {
            mda = MessageDigest.getInstance(MD5);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
           e.printStackTrace();
       }
    }

    public void updateDigest(byte[] key, byte[] value) {
      mda.update(key);
      mda.update(value);
    }
}
