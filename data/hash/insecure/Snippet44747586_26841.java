public class XFile {
    private final File file;

    public XFile(File file) {
        this.file = file;
    }

    public String name() {
        return file.getName();
    }

    //other data you want to know, create getters for all wanted information from File.

    public byte[] md5() {
        try (InputStream input = new BufferedInputStream(new FileInputStream(file))) {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            int tempByte;
            while ((tempByte = input.read()) != -1) {
                md5.update((byte) tempByte);
            }
            return md5.digest();
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }
}
