public FileRepositoryImpl(String digestAlgo) throws NoSuchAlgorithmException {
    this.digestAlgo = digestAlgo;
    MessageDigest.getInstance(digestAlgo);
    }
@Override
public String insert(File file) throws IOException {
// initialize message digest
    MessageDigest messageDigest = null;
    try {
        messageDigest = MessageDigest.getInstance(digestAlgo);
    } catch (NoSuchAlgorithmException e) {
        LOGGER.fatal(MD_INIT_ERROR, e);
        return null;
}
    // other code ....
}
// other methods (may contain local MessageDigest)
