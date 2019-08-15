    import java.security.Key;
    import java.security.KeyStore;
    import java.security.Provider;
    import java.security.Security;

    import javax.crypto.spec.SecretKeySpec;

    import sun.security.pkcs11.SunPKCS11;

    public class TestClass {


         public static void main(String[] args) throws Exception {
                // Set up the Sun PKCS 11 provider
               // String configName = "Z:\\SOFTHSM_INSTALL\\etc\\softhsm2.conf";

             String configName = "softhsm2.cfg";

                Provider p = new SunPKCS11(configName);

                if (-1 == Security.addProvider(p)) {
                    throw new RuntimeException("could not add security provider");
                }

                // Load the key store
                char[] pin = "mypin".toCharArray();
                KeyStore keyStore = KeyStore.getInstance("PKCS11", p);
                keyStore.load(null, pin);

                // AES key
                SecretKeySpec secretKeySpec = new SecretKeySpec("0123456789ABCDEF".getBytes(), "AES");
                Key key = new SecretKeySpec(secretKeySpec.getEncoded(), "AES");

                keyStore.setKeyEntry("AA", key, "1234".toCharArray(), null);
                keyStore.store(null); //this gives me the exception.

         }

    }
