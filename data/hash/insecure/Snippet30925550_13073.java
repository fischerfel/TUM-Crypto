/**
 * Read the file and calculate the SHA-1 checksum
 * 
 * @param file
 *            the file to read
 * @return the hex representation of the SHA-1 using uppercase chars
 * @throws FileNotFoundException
 *             if the file does not exist, is a directory rather than a
 *             regular file, or for some other reason cannot be opened for
 *             reading
 * @throws IOException
 *             if an I/O error occurs
 * @throws NoSuchAlgorithmException
 *             should never happen
 */
private static String calcSHA1(File file) throws FileNotFoundException,
        IOException, NoSuchAlgorithmException {

    MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
    try (InputStream input = new FileInputStream(file)) {

        byte[] buffer = new byte[8192];
        int len = input.read(buffer);

        while (len != -1) {
            sha1.update(buffer, 0, len);
            len = input.read(buffer);
        }

        return new HexBinaryAdapter().marshal(sha1.digest());
    }
}
