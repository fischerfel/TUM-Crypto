    final FileInputStream fis = new FileInputStream(filename);
    final CountingInputStream countIs = new CountingInputStream(fis);

    final boolean isZipped = isZipped(countIs);

    // make sure we reset the inputstream before calculating the digest
    fis.getChannel().position(0);
    final DigestInputStream dis = new DigestInputStream(countIs, MessageDigest.getInstance("SHA-256"));

    // decide which inputStream to use
    InputStream is = null;
    ZipInputStream zis = null;
    if (isZipped) {
        zis = new ZipInputStream(dis);
        zis.getNextEntry();
        is = zis;
    } else {
        is = dis;
    }

    final File tmpFile = File.createTempFile("Encrypted_", ".tmp");
    final OutputStream os = new CipherOutputStream(new FileOutputStream(tmpFile), obtainCipher());
    try {
        readValidateAndWriteRecords(is, os);
        failIf2ndZipEntryExists(zis);
    } catch (final Exception e) {
        os.close();
        tmpFile.delete();
        throw e;
    }

    System.out.println("Digest: " + obtainDigest(dis));
    dis.close();

    System.out.println("\nValidating bytes read and calculated digest");
    final DigestInputStream dis2 = new DigestInputStream(new CountingInputStream(new FileInputStream(filename)), MessageDigest.getInstance("SHA-256"));
    System.out.println("Digest: " + obtainDigest(dis2));
    dis2.close();
