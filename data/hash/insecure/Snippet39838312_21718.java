public static String calculateChecksum(File javaJarFile) {

    String filepath = javaJarFile.getAbsolutePath();
    StringBuilder sb = new StringBuilder();
    FileInputStream fis = null;

    try {
        MessageDigest md = MessageDigest.getInstance("SHA-1"); //40 character checksum
        fis = new FileInputStream(filepath);
        byte[] dataBytes = new byte[1024];
        int nread = 0;

        while ((nread = fis.read(dataBytes)) != -1)
            md.update(dataBytes, 0, nread);

        byte[] mdbytes = md.digest();

        for (int i = 0; i < mdbytes.length; i++)
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16)
                    .substring(1));

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();

    } catch (IOException e) {
        e.printStackTrace();

    } finally {
        try {
            if (fis != null)
                fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    return sb.toString();
}
