import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class Main {
    private static final String PASSWORD = "swordfish";

    private static final Callable<Boolean> CONCURRENT = new Callable<Boolean>() {
        @Override
        public Boolean call() throws Exception {
            final Key key1 = deriveKeyFromPassword(PASSWORD.toCharArray());
            final byte[] key1bytes = key1.getEncoded();
            for (int i = 0; i < 10000; i++) {
                final Key key2 = deriveKeyFromPassword(PASSWORD.toCharArray());
                final byte[] key2bytes = key2.getEncoded();
                if (!Arrays.equals(key1bytes, key2bytes)) {
                    throw new Exception("Keys do not match!\n"
                            + "key1: " + DatatypeConverter.printHexBinary(key1.getEncoded()) + "\n"
                            + "key2: " + DatatypeConverter.printHexBinary(key2.getEncoded()) + "\n");
                }
            }
            return true;
        }
    };

    public static Key deriveKeyFromPassword(final char[] password) {
        try {
            final SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            final PBEKeySpec spec = new PBEKeySpec(password, new byte[8], 5, 256);
            final SecretKey secret = factory.generateSecret(spec);
            return new SecretKeySpec(secret.getEncoded(), "AES");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(final String[] args) throws Exception {
        final ExecutorService executor = Executors.newCachedThreadPool();
        try {
            int iterations = 1;
            while (true) {
                final ArrayList<Future<Boolean>> results = new ArrayList<Future<Boolean>>();
                for (int i = 0; i < 10; i++) {
                    results.add(executor.submit(CONCURRENT));
                }
                for (final Future<Boolean> result : results) {
                    result.get();
                }
                System.out.println((iterations * 10 * 10000 * 2) + " keys derived");
                iterations++;
            }
        } catch (final Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
