public class IntegrityUtils {
public static String toASCII(byte b[], int start, int length) {
    StringBuffer asciiString = new StringBuffer();

    for (int i = start; i < (length + start); i++) {
        // exclude nulls from the ASCII representation
        if (b[i] != (byte) 0x00) {
            asciiString.append((char) b[i]);
        }
    }

    return asciiString.toString();
}

public static String getMD5Checksum(File file) throws Exception {
    byte[] b = createChecksum(file);
    String result = "";

    for (int i = 0; i < b.length; i++) {
        result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
    }
    return result;
}

public static byte[] createChecksum(File file) throws Exception {
    InputStream fis = new FileInputStream(file);

    byte[] buffer = new byte[1024];
    MessageDigest complete = MessageDigest.getInstance("MD5");
    int numRead;

    do {
        numRead = fis.read(buffer);
        if (numRead > 0) {
            complete.update(buffer, 0, numRead);
        }
    } while (numRead != -1);

    fis.close();
    return complete.digest();
}
}
