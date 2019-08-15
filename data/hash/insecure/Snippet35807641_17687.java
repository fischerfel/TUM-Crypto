public class DiskHandler {
File diskRoot;

public DiskHandler() {
    diskRoot = new File("\\\\.\\C:");
}

public String calculateSHA1() {
    System.out.println("sha1 hash started");
    try (FileInputStream inputStream = new FileInputStream(diskRoot)) {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");

        byte[] bytesBuffer = new byte[1024];
        int bytesRead = -1;

        while ((bytesRead = inputStream.read(bytesBuffer)) != -1) {
            digest.update(bytesBuffer, 0, bytesRead);
        }

        byte[] hashedBytes = digest.digest();

        return convertByteArrayToHexString(hashedBytes);
    } catch (NoSuchAlgorithmException | IOException ex) {
        ex.printStackTrace();
        return "";
    }
}

private static String convertByteArrayToHexString(byte[] arrayBytes) {
    StringBuffer stringBuffer = new StringBuffer();
    for (int i = 0; i < arrayBytes.length; i++) {
        stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16).substring(1));
    }
    return stringBuffer.toString();
}

}
