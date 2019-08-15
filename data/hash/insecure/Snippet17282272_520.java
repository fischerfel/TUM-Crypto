public static String getHash(File doc) {
    MessageDigest md = null;
    try {
        md = MessageDigest.getInstance("SHA1");
        FileInputStream inStream = new FileInputStream(doc);
        DigestInputStream dis = new DigestInputStream(inStream, md);
        BufferedInputStream bis = new BufferedInputStream(dis);
        while (true) {
            int b = bis.read();
            if (b == -1)
                break;
        }

        inStream.close();
        dis.close();
        bis.close();
    } catch (NoSuchAlgorithmException | IOException e) {
        e.printStackTrace();
    }

    BigInteger bi = new BigInteger(md.digest());

    return bi.toString(16);
}
