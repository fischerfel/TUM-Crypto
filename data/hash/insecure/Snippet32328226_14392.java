MessageDigest lMd = MessageDigest.getInstance("MD5");

FileOutputStream lFos = null;
try (DigestInputStream lDis = new DigestInputStream(lListener.getInputStream(), lMd);
                            ZipInputStream lZip = new ZipInputStream(lDis)) {

    // Read the response content
    //get the zipped file list entry
    ZipEntry lZipEntry = lZip.getNextEntry();

    while (lZipEntry != null) {
        String lFileName = lZipEntry.getName();

        File lNewFile = new File(UPDATE_FOLDER + File.separator + lFileName);

        if (lZipEntry.isDirectory()) {
            lNewFile.mkdirs();
        } else {
            //create all non exists folders
            new File(lNewFile.getParent()).mkdirs();

            lFos = new FileOutputStream(lNewFile);             

            int lRead;
            while ((lRead = lZip.read(lBuffer)) > -1) {
                lFos.write(lBuffer, 0, lRead);
            }

            lFos.close();   
        }

        lZipEntry = lZip.getNextEntry();
    }

    lZip.closeEntry();
} finally {
    if (lFos != null) {
        lFos.close();
}
                        byte[] lDigest = lMd.digest();

                    StringBuffer lHexString = new StringBuffer();
                    for (int lI = 0; lI < lDigest.length; lI++) {
                        if ((0xFF & lDigest[lI]) < 0x10) {
                            lHexString.append("0"
                                    + Integer.toHexString((0xFF & lDigest[lI])));
                        } else {
                            lHexString.append(Integer.toHexString(0xFF & lDigest[lI]));
                        }
                    }
                    String lDigestStr = lHexString.toString();
