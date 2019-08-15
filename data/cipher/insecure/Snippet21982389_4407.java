    import java.security.spec.KeySpec;
    import javax.crypto.Cipher;
    import javax.crypto.SecretKey;
    import javax.crypto.SecretKeyFactory;
    import javax.crypto.spec.DESedeKeySpec;


    public class SecretKeyEncryptionExample
    {
      private static final String FORMAT = "ISO-8859-1";
      public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
      private KeySpec ks;
      private SecretKeyFactory skf;
      private Cipher cipher;
      SecretKey key;

      public SecretKeyEncryptionExample()
        throws Exception
      {
        String myEncryptionKey = "<48 chars long string>";

        this.ks = new DESedeKeySpec(myEncryptionKey.getBytes("ISO-8859-1"));
        this.skf = SecretKeyFactory.getInstance("DESede");
        this.cipher = Cipher.getInstance("DESede");
        this.key = this.skf.generateSecret(this.ks);
      }
