public static void main(String[] args) throws Exception {

    File dire = new File("C:\\Users\\spacitron\\Desktop\\Stuff");
    ArrayList<String> hashes = new ArrayList<>();
    for (File doc : dire.listFiles()) {
        String docHash = getHash(doc);
        if (hashes.contains(docHash)) {
            doc.delete();
        } else {
            hashes.add(docHash);
        }
    }

}

public static String getHash(File d) {
    MessageDigest md = null;
    try {
        md = MessageDigest.getInstance("SHA1");
        FileInputStream inStream = new FileInputStream(d);
        DigestInputStream dis = new DigestInputStream(inStream, md);
        BufferedInputStream bis = new BufferedInputStream(dis);
        while (true) {
            int b = bis.read();
            if (b == -1)
                break;
        }
    } catch (NoSuchAlgorithmException | IOException e) {
        e.printStackTrace();
    }

    BigInteger bi = new BigInteger(md.digest());

    return bi.toString(16);
}
