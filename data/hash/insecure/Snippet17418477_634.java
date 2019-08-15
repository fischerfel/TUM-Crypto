public static String md5Hash(File file) {
    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        InputStream is = new FileInputStream(file);
        byte[] buffer = new byte[1024];

        try {
            is = new DigestInputStream(is, md);

            while (is.read(buffer) != -1) { }
        } finally {
            is.close();
        }

        byte[] digest = md.digest();

        BigInteger bigInt = new BigInteger(1, digest);
        String output = bigInt.toString(16);
        while (output.length() < 32) {
            output = "0" + output;
        }

        return output;
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }

    return null;
}
