private static final int MAX_NUMBER_OF_TRIES = 3;
public Path downloadFile(String storageFileName, String bucketName) throws IOException {
    // In my real code, this is a field populated in the constructor.
    Storage storage = Objects.requireNonNull(StorageOptions.getDefaultInstance().getService());

    BlobId blobId = BlobId.of(bucketName, storageFileName);
    Path outputFile = Paths.get(storageFileName.replaceAll("/", "-"));
    int retryCounter = 1;
    Blob blob;
    boolean checksumOk;
    MessageDigest messageDigest;
    try {
        messageDigest = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException ex) {
        throw new RuntimeException(ex);
    }

    do {
        LOGGER.debug("Start download file {} from bucket {} to Content Store (try {})", storageFileName, bucketName, retryCounter);
        blob = storage.get(blobId);
        if (null == blob) {
            throw new CloudStorageCommunicationException("Failed to download file after " + retryCounter + " tries.");
        }
        if (Files.exists(outputFile)) {
            Files.delete(outputFile);
        }
        try (ReadChannel reader = blob.reader();
             FileChannel channel = new FileOutputStream(outputFile.toFile(), true).getChannel()) {
            ByteBuffer bytes = ByteBuffer.allocate(128 * 1024);
            int bytesRead = reader.read(bytes);
            while (bytesRead > 0) {
                bytes.flip();
                messageDigest.update(bytes.array(), 0, bytesRead);
                channel.write(bytes);
                bytes.clear();
                bytesRead = reader.read(bytes);
            }
        }
        String checksum = Base64.encodeBase64String(messageDigest.digest());
        checksumOk = checksum.equals(blob.getMd5());
        if (!checksumOk) {
            Files.delete(outputFile);
            messageDigest.reset();
        }
    } while (++retryCounter <= MAX_NUMBER_OF_TRIES && !checksumOk);
    if (!checksumOk) {
        throw new CloudStorageCommunicationException("Failed to download file after " + MAX_NUMBER_OF_TRIES + " tries.");
    }
    return outputFile;
}
