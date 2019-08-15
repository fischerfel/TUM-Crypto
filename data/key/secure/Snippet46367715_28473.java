import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Main {

    final private static boolean IS_CREATE = false;
    final private static boolean IS_DEBUG = true;
    final private static String KEYSTORE_FILE = "../../../Desktop/crypto/myKeyStore.pfx";
    final private static char[] PASSWORD = "password".toCharArray();
    final private static KeyStore.ProtectionParameter PP = new KeyStore.PasswordProtection(PASSWORD);

    private static KeyStore ks;

    public static void main(String[] args) throws Exception {

        if (IS_CREATE) {
            createFile();
        } else {
            init();

            key = "123456789aabbccddeefffffffffffff";
            addEntry("key3", key);
            saveKeyStore();
            printKey("key3");

            printKey("key3");

        }
    }

    private static void init() throws Exception   {
        ks = KeyStore.getInstance("PKCS12");

        try (FileInputStream fis = new FileInputStream(KEYSTORE_FILE)) {
            ks.load(fis, PASSWORD);
        }
    }

    public static void addEntry(String alias, String key) throws KeyStoreException {
        SecretKey spec = new SecretKeySpec(key.getBytes(), "AES");
        KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry(spec);
        ks.setEntry(alias, entry, PP);
    }

    public static void deleteEntry(String alias) throws KeyStoreException {
        ks.deleteEntry(alias);
    }

    public static void saveKeyStore() throws Exception {
        try (FileOutputStream fos = new FileOutputStream(KEYSTORE_FILE)) {
            ks.store(fos, PASSWORD);
        }
    }

    public static void printKey(String alias) throws Exception {
        Key key = ks.getKey(alias, PASSWORD);
        System.out.println(new String(key.getEncoded()));
    }

    public static void debugPrint(String msg) {
        String toPrint = (IS_DEBUG) ? msg : "";
        System.out.println(toPrint);
    }

    public static void debugPrint(Integer val) {
        int toPrint = (IS_DEBUG) ? val : null;
        System.out.println(toPrint);
    }

    private static void createFile() throws Exception {
        ks = KeyStore.getInstance("PKCS12");

        ks.load(null, PASSWORD);
        addEntry("test", "value");
        saveKeyStore();
    }
}
