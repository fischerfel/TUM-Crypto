private class CompressionStream extends GZIPOutputStream {
    // Use compression levels from the deflator class
    public CompressionStream(OutputStream out, int compressionLevel) throws IOException {
        super(out);
        def.setLevel(compressionLevel);
    }
}

public void createTAR(){
    boolean isSuccessful=false;
    int count = 0;
    int maxTries = 3;
    while(!isSuccessful) {
        InputStream inputStream =null;
        FileOutputStream outputStream =null;
        CompressionStream compressionStream=null;
        OutputStream md5OutputStream = null;
        TarArchiveOutputStream tarStream = null;
        try{
            inputStream = new BufferedInputStream(new FileInputStream(rawfile));
            File stagingPath = new File("C:\\Workarea\\6d22b6a3-564f-42b4-be83-9e1573a718cd\\b88beb62-aa65-4ad5-b46c-4f2e9c892259.tar.gz");
            boolean isDeleted = false;
            if(stagingPath.exists()){
                isDeleted =  stagingPath.delete();
                if(stagingPath.exists()){
                    try {
                        FileUtils.forceDelete(stagingPath);
                    }catch (IOException ex){
                        //ignore
                    }
                }
            }
            outputStream = new FileOutputStream(stagingPath);
            if (isCompressionEnabled) {
                compressionStream = new
                        CompressionStream(outputStream, getCompressionLevel(om));
            }
            final MessageDigest outputDigest = MessageDigest.getInstance("MD5");
            md5OutputStream = new DigestOutputStream(isCompressionEnabled ? compressionStream : outputStream, outputDigest);
            tarStream = new TarArchiveOutputStream(new BufferedOutputStream(md5OutputStream));
            tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
            tarStream.setBigNumberMode(TarArchiveOutputStream.BIGNUMBER_STAR);
            TarArchiveEntry entry = new TarArchiveEntry("Alldbtypes");
            entry.setSize(getOriginalSize());
            entry.setModTime(getLastModified().getMillis());
            tarStream.putArchiveEntry(entry);
            org.apache.commons.io.IOUtils.copyLarge(inputStream, tarStream);
            inputStream.close();
            tarStream.closeArchiveEntry();
            tarStream.finish();
            tarStream.close();
            String digest = Hex.encodeHexString(outputDigest.digest());
            setChecksum(digest);
            setIngested(DateTime.now());
            setOriginalSize(FileUtils.sizeOf(stagingPath));
            isSuccessful =true;
        } catch (IOException e) {
            if (++count == maxTries) {
                throw new RuntimeException("Exception: " + e.getMessage(), e);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(Exception("MD5 hash algo not installed.");
        } catch (Exception e) {
            throw new RuntimeException("Exception: " + e.getMessage(), e);
        } finally {
            org.apache.commons.io.IOUtils.closeQuietly(inputStream);
            try {
                tarStream.flush();
                tarStream.finish();
            } catch (IOException e) {
                e.printStackTrace();
            }
            org.apache.commons.io.IOUtils.closeQuietly(tarStream);
            org.apache.commons.io.IOUtils.closeQuietly(compressionStream);
            org.apache.commons.io.IOUtils.closeQuietly(md5OutputStream);
            org.apache.commons.io.IOUtils.closeQuietly(outputStream);
        }

    }
}
