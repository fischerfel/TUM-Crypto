public class DigestTest {

    private static final byte[] TEST_DATA = "Some test data for digest computations".getBytes();

    @Test
    public void shouldStoreAndRestoreDigest() throws Exception {
        final MessageDigest referenceDigest = MessageDigest.getInstance("SHA-512");
        MessageDigest testDigest = MessageDigest.getInstance("SHA-512");
        referenceDigest.update(TEST_DATA);
        testDigest.update(TEST_DATA);
        // store state
        final XStream xs = new XStream(new StaxDriver());
        xs.alias("md", MessageDigest.class);
        final String serializedMd = xs.toXML(testDigest);
        System.out.println(serializedMd);
        // restore state
        testDigest = (MessageDigest)xs.fromXML(serializedMd);
        // ---
        referenceDigest.update(TEST_DATA);
        testDigest.update(TEST_DATA);
        Assert.assertArrayEquals(referenceDigest.digest(), testDigest.digest());
    }
}
