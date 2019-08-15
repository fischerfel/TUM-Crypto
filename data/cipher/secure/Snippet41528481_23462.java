package nl.owlstead.stackoverflow;

import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Provider.Service;
import java.security.Security;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class FindKeySizes {

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        Provider[] providers = Security.getProviders();
        for (Provider provider : providers) {
            Set<Service> services = provider.getServices();
            for (Service service : services) {
                if (!service.getType().equalsIgnoreCase("cipher")) {
                    continue;
                }

                String alg = service.getAlgorithm();

                KeyGenerator skf = null;
                Cipher cipher;
                try {
                    // filters out symmetric algorithms
                    skf = KeyGenerator.getInstance(alg, provider);
                } catch (NoSuchAlgorithmException e) {
                    // OK, that may not exist
                }
                try {
                    cipher = Cipher.getInstance(alg, provider);
                } catch (NoSuchAlgorithmException e) {
                    continue;
                }

                SortedSet<Integer> keySizes = new TreeSet<>();
                for (int keySize = Byte.SIZE; keySize <= 512; keySize += Byte.SIZE) {
                    try {
                        SecretKey key;
                        if (skf != null) {
                            try {
                                skf.init(keySize);
                                key = skf.generateKey();
                            } catch (Exception e) {
                                continue;
                            }
                        } else {
                            key = new SecretKeySpec(new byte[keySize / Byte.SIZE], alg);
                        }
                        cipher.init(Cipher.ENCRYPT_MODE, key);
                        keySizes.add(keySize);
                    } catch (Exception e) {
                        // needed for RuntimeException from providers
                        if (alg.equalsIgnoreCase("AES") && (keySize == 128)) {
                            e.printStackTrace(System.err);
                        }

                        continue;
                    }
                }

                if (!keySizes.isEmpty()) {
                    System.out.printf("Provider: %s, cipher: %s, key sizes: %s%n",
                            provider.getName(), alg, keySizes);
                }
            }
        }
    }
}
