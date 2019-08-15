public class MD5CheckSum {

public static byte[] createChecksum(String pFilepath) throws Exception {
    InputStream lFis = new FileInputStream(pFilepath);

    byte[] lBuffer = new byte[1024];
    MessageDigest lMessageDigest = MessageDigest.getInstance("MD5");

    int lNumRead;

    do {
        lNumRead = lFis.read(lBuffer);
        if (lNumRead > 0) {
            lMessageDigest.update(lBuffer, 0, lNumRead);
        }
    } while (lNumRead != -1);

    lFis.close();
    return lMessageDigest.digest();
}

public static String getMD5Checksum(String pFilepath) throws Exception {
    byte[] lBytes = createChecksum(pFilepath);
    return Base64.encodeToString(lBytes, Base64.DEFAULT);
}
