public class SHA1Compare {
    private static final int CHUNK_SIZE = 4096;

    public void assertEqualsSHA1(String expectedPath, String actualPath) throws IOException, NoSuchAlgorithmException {
        File expectedFile = new File(expectedPath);
        File actualFile = new File(actualPath);
        Assert.assertEquals(expectedFile.length(), actualFile.length());
        try (FileInputStream fisExpected = new FileInputStream(actualFile);
                FileInputStream fisActual = new FileInputStream(expectedFile)) {
            Assert.assertEquals(makeMessageDigest(fisExpected), 
                    makeMessageDigest(fisActual));
        }
    }

    public String makeMessageDigest(InputStream is) throws NoSuchAlgorithmException, IOException {
        byte[] data = new byte[CHUNK_SIZE];
        MessageDigest md = MessageDigest.getInstance("SHA1");
        int bytesRead = 0;
        while(-1 != (bytesRead = is.read(data, 0, CHUNK_SIZE))) {
            md.update(data, 0, bytesRead);
        }
        return toHexString(md.digest());
    }

    private String toHexString(byte[] digest) {
        StringBuilder sha1HexString = new StringBuilder();
        for(int i = 0; i < digest.length; i++) {
            sha1HexString.append(String.format("%1$02x", Byte.valueOf(digest[i])));
        }
        return sha1HexString.toString();
    }
}
