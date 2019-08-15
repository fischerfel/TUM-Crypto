package scsc;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.Map;
import java.util.zip.InflaterInputStream;

/*
 * Published under the Do What the Fuck You Want to Public License (       http://www.wtfpl.net/ )
 */
public class ScsC {
private static byte[] AES_KEY = new byte[]{
        (byte) 0x2a, (byte) 0x5f, (byte) 0xcb, (byte) 0x17,
        (byte) 0x91, (byte) 0xd2, (byte) 0x2f, (byte) 0xb6,
        (byte) 0x02, (byte) 0x45, (byte) 0xb3, (byte) 0xd8,
        (byte) 0x36, (byte) 0x9e, (byte) 0xd0, (byte) 0xb2,
        (byte) 0xc2, (byte) 0x73, (byte) 0x71, (byte) 0x56,
        (byte) 0x3f, (byte) 0xbf, (byte) 0x1f, (byte) 0x3c,
        (byte) 0x9e, (byte) 0xdf, (byte) 0x6b, (byte) 0x11,
        (byte) 0x82, (byte) 0x5a, (byte) 0x5d, (byte) 0x0a,
};

public static void main(String[] args) throws Exception {
    if (args.length < 1) {
        System.out.println("ERROR: expecting at least one file.");
    } else {
        removeCryptographyRestrictions();
        for (String filename : args) {
            decrypt(filename);
        }
    }
}

private static void decrypt(String filename) throws Exception {
    File scsc = new File(filename);
    if (!scsc.isFile() || !scsc.canWrite()) {
        throw new IllegalArgumentException(filename + " is not a writable file.");
    }
    boolean encrypted = isEncrypted(scsc);
    if (!encrypted) {
        System.out.println(scsc + " does not seem to be encrypted.");
    } else {
        File decrypted = decrypt(scsc);
        Files.copy(decrypted.toPath(), scsc.toPath(), StandardCopyOption.REPLACE_EXISTING);
        System.out.println("decrypted: " + scsc);
    }
}

private static boolean isEncrypted(File file) throws Exception {
    byte[] header = new byte[4];
    FileInputStream fis = new FileInputStream(file);
    if (fis.read(header) != header.length) {
        throw new RuntimeException("could not read header of " + file);
    }
    fis.close();
    String headerAsString = new String(header, Charset.forName("UTF-8"));
    return "ScsC".equals(headerAsString);
}

private static File decrypt(File input) throws Exception {
    File out = File.createTempFile("scsc-", ".tmp");
    out.deleteOnExit();

    byte[] data = new byte[(int) (input.length())];
    FileInputStream fis = new FileInputStream(input);
    if (fis.read(data) != data.length) {
        throw new RuntimeException("Could not read " + input + " into memory");
    }
    fis.close();

    byte[] cipherText = new byte[data.length - 0x38];
    byte[] iv = new byte[0x10];
    System.arraycopy(data, 0x38, cipherText, 0, cipherText.length);
    System.arraycopy(data, 0x24, iv, 0, iv.length);
    byte[] decrypted = decrypt(cipherText, AES_KEY, iv);

    ByteArrayInputStream bis = new ByteArrayInputStream(decrypted);
    InflaterInputStream iis = new InflaterInputStream(bis);
    InputStreamReader ir = new InputStreamReader(iis);
    BufferedReader br = new BufferedReader(ir);

    FileOutputStream fos = new FileOutputStream(out);
    OutputStreamWriter osw = new OutputStreamWriter(fos);
    PrintWriter pw = new PrintWriter(osw);

    for (String line = br.readLine(); line != null; line = br.readLine()) {
        pw.println(line);
    }
    pw.close();
    br.close();

    return out;
}

private static byte[] decrypt(byte[] cipherText, byte[] keyBytes, byte[] iv) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
    cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
    return cipher.doFinal(cipherText);
}

private static void removeCryptographyRestrictions() throws Exception {
    // taken from http://stackoverflow.com/questions/1179672/unlimited-strength-jce-policy-files
    final Class<?> jceSecurity = Class.forName("javax.crypto.JceSecurity");
    final Class<?> cryptoPermissions = Class.forName("javax.crypto.CryptoPermissions");
    final Class<?> cryptoAllPermission = Class.forName("javax.crypto.CryptoAllPermission");

    final Field isRestrictedField = jceSecurity.getDeclaredField("isRestricted");
    isRestrictedField.setAccessible(true);
    isRestrictedField.set(null, false);

    final Field defaultPolicyField = jceSecurity.getDeclaredField("defaultPolicy");
    defaultPolicyField.setAccessible(true);
    final PermissionCollection defaultPolicy = (PermissionCollection) defaultPolicyField.get(null);

    final Field perms = cryptoPermissions.getDeclaredField("perms");
    perms.setAccessible(true);
    ((Map<?, ?>) perms.get(defaultPolicy)).clear();

    final Field instance = cryptoAllPermission.getDeclaredField("INSTANCE");
    instance.setAccessible(true);
    defaultPolicy.add((Permission) instance.get(null));
}
}
