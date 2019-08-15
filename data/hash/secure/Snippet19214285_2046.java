    public static final byte[] getFileHash(final File src, final String hashAlgo) throws IOException, NoSuchAlgorithmException {
    final int         BUFFER = 32 * 1024;
    final Path        file = src.toPath();
    try(final FileChannel fc   = FileChannel.open(file)) {
        final long        size = fc.size();
        final MessageDigest hash = MessageDigest.getInstance(hashAlgo);
        long position = 0;
        while(position < size) {
            final MappedByteBuffer data = fc.map(FileChannel.MapMode.READ_ONLY, 0, Math.min(size, BUFFER));
            if(!data.isLoaded()) data.load();
            System.out.println("POS:"+position);
            hash.update(data);
            position += data.limit();
            if(position >= size) break;
        }
        return hash.digest();
    }
}

public static final byte[] getCachedFileHash(final File src, final String hashAlgo) throws NoSuchAlgorithmException, FileNotFoundException, IOException{
    final Path path = src.toPath();
    if(!Files.isReadable(path)) return null;
    final UserDefinedFileAttributeView view = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);
    final String name = "user.hash."+hashAlgo;
    final ByteBuffer bb = ByteBuffer.allocate(64);
    try { view.read(name, bb); return ((ByteBuffer)bb.flip()).array();
    } catch(final NoSuchFileException t) { // Not yet calculated
    } catch(final Throwable t) { t.printStackTrace(); }
    System.out.println("Hash not found calculation");
    final byte[] hash = getFileHash(src, hashAlgo);
    view.write(name, ByteBuffer.wrap(hash));
    return hash;
}
