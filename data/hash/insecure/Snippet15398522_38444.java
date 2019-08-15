import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import sun.misc.BASE64Encoder;

public class Main {
    public static void main(String[] args) throws Exception {
        for (String jarFilename : args)
            extractHash(jarFilename);
    }

    private static void extractHash(String jarFilename) throws Exception {
        BASE64Encoder base64 = new BASE64Encoder();
        MessageDigest sha1 = MessageDigest.getInstance("SHA");
        Set<Certificate> certificates = new HashSet<Certificate>();
        JarFile jarFile = new JarFile(jarFilename);
        for (JarEntry jarEntry : Collections.list(jarFile.entries())) {
            jarFile.getInputStream(jarEntry).skip(Long.MAX_VALUE);
            Certificate[] certs = jarEntry.getCertificates();
            if (certs == null)
                continue;
            certificates.addAll(Arrays.asList(certs));
        }
        System.out.printf("%s:", jarFilename);
        for (Certificate cert : certificates) {
            byte[] digest = sha1.digest(cert.getEncoded());
            System.out.printf(" %s", base64.encode(digest));
        }
        if (certificates.isEmpty())
            System.out.printf(" NOT SIGNED!");
        System.out.println();
        jarFile.close();
    }
}
