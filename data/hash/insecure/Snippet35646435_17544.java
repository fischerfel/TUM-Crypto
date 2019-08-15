try {
        // Create staging file output stream
        File temp = new File(getFilePath(objectm));
        log.debug("temping " + objectm.getPath());
        outputStream = new FileOutputStream(temp);

        // Create GZip pass-thru stream
        if (isCompressionEnabled) {
            compressionStream = new
                    CompressionStream(outputStream, getCompressionLevel(objectm));
        }

        // Create MD5 hash
        final MessageDigest outputDigest = MessageDigest.getInstance("MD5");
        md5OutputStream = new DigestOutputStream(isCompressionEnabled ? compressionStream : outputStream, outputDigest);

        // Create tar stream
        tarStream = new TarArchiveOutputStream(new BufferedOutputStream(md5OutputStream));
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarStream.setBigNumberMode(TarArchiveOutputStream.BIGNUMBER_STAR);

        // tar the first object
        TarArchiveEntry entry = new TarArchiveEntry(objectm.getHierarchy());
        entry.setSize(objectm.getOriginalSize());
        entry.setModTime(objectm.getLastModified().getMillis());
        tarStream.putArchiveEntry(entry);
        org.apache.commons.io.IOUtils.copyLarge(inputStream, tarStream);


        // Collect properties to return
        String digest = Hex.encodeHexString(outputDigest.digest());
        objectm.setChecksum(digest);
        objectm.setDate(DateTime.now());
        objectm.setCompressSize(FileUtils.sizeOf(temp));          
        tarStream.finish();
        log.debug("Completed.");

    } catch (Exception e) {
        throw new Exception("Exception: Creating tar" , e);
    } finally {
        org.apache.cobjectmmons.io.IOUtils.closeQuietly(inputStream);
        org.apache.cobjectmmons.io.IOUtils.closeQuietly(tarStream);
        org.apache.cobjectmmons.io.IOUtils.closeQuietly(cobjectmpressionStream);
        org.apache.cobjectmmons.io.IOUtils.closeQuietly(md5OutputStream);
        org.apache.cobjectmmons.io.IOUtils.closeQuietly(outputStream);
    }
