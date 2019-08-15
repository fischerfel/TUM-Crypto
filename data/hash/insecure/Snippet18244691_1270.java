  public static String toMD5(File file) throws IOException, NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("MD5");
    FileInputStream fis = new FileInputStream(file.getPath());


    byte[] dataBytes = new byte[8192];

    int nread = 0;
    while ((nread = fis.read(dataBytes)) != -1) {
        md.update(dataBytes, 0, nread);
    }

    byte[] mdbytes = md.digest();

    //convert the byte to hex format method 2
    StringBuffer hexString = new StringBuffer();
    for (int i = 0; i < mdbytes.length; i++) {
        String hex = Integer.toHexString(0xff & mdbytes[i]);
        if (hex.length() == 1) hexString.append('0');
        hexString.append(hex);
    }
    return hexString.toString();
}
