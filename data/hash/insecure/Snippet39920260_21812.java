public class ApachePoiTest {

    @Test
    public void readingShouldNotModifyFile() throws Exception {
        final File testFile = new File("C:/work/src/test/resources/Book2.xlsx");
        final byte[] originalChecksum = calculateChecksum(testFile);
        Assert.assertTrue("Calculating checksum modified file",
            MessageDigest.isEqual(originalChecksum, calculateChecksum(testFile)));
        try (Workbook wb = WorkbookFactory.create(testFile)) {
            Assert.assertNotNull("Reading file with Apache POI", wb);
        }
        Assert.assertTrue("Reading file with Apache POI modified file",
            MessageDigest.isEqual(originalChecksum, calculateChecksum(testFile)));
    }

    @Test
    public void readingInputStreamShouldNotModifyFile() throws Exception {
        final File testFile = new File("C:/work/src/test/resources/Book2.xlsx");
        final byte[] originalChecksum = calculateChecksum(testFile);
        Assert.assertTrue("Calculating checksum modified file",
            MessageDigest.isEqual(originalChecksum, calculateChecksum(testFile)));
        try (InputStream is = new FileInputStream(testFile); Workbook wb = WorkbookFactory.create(is)) {
            Assert.assertNotNull("Reading file with Apache POI", wb);
        }
        Assert.assertTrue("Reading file with Apache POI modified file",
            MessageDigest.isEqual(originalChecksum, calculateChecksum(testFile)));
    }

    private byte[] calculateChecksum(final File file) throws Exception {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        md.reset();
        try (InputStream is = new FileInputStream(file)) {
            final byte[] bytes = new byte[2048];
            int numBytes;
            while ((numBytes = is.read(bytes)) != -1) {
                md.update(bytes, 0, numBytes);
            }
            return md.digest();
        }
    }
}
