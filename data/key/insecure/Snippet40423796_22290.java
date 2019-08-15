import java.security.*;
import sun.security.pkcs11.*;
import javax.crypto.spec.SecretKeySpec;

public class Main {
    public static void main(String[] args) throws Exception {
        // Set up the Sun PKCS 11 provider
        String configName = "softhsm.cfg";
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
        keyStore.store(null); //this no longer gives me the exception.

        Enumeration<String> aliases = keyStore.aliases();
        while(aliases.hasMoreElements()){
            String alias = aliases.nextElement();
            System.out.println(alias + ": " + keyStore.getKey(alias,"1234".toCharArray()));
        }
    }
}
