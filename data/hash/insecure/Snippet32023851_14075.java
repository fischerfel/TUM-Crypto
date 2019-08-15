FileInputStream fileInputStream = null;

byte[] bFile = new byte[(int) file.length()];

try {

    fileInputStream = new FileInputStream(file);
    fileInputStream.read(bFile);

} catch (Exception e) {
    e.printStackTrace();
}

try {
    MessageDigest md = MessageDigest.getInstance("MD5");

    int read = 0;
    while((read = fileInputStream.read(bFile)) != -1) {
        md.update(bFile, 0, read);
    }
    fileInputStream.close();

    byte[] mdBytes = md.digest();

    StringBuffer sb = new StringBuffer();
    for(int i=0; i < mdBytes.length; ++i) {
        sb.append(Integer.toString((mdBytes[i] & 0xff) + 0x100, 16).substring(1));
    }

    // generated MD5 is d41d8cd98f00b204e9800998ecf8427e


} catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
} catch (IOException e) {
    e.printStackTrace();
}
