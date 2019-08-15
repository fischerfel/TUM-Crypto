private static String getMD5Digest(File file) {
    BufferedInputStream reader = null;
    String hexDigest = new String();
    try {
        reader = new BufferedInputStream( new FileInputStream(file));
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
    MessageDigest md = null;
    try {
        md = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    byte[] buffer = new byte[4096];
    long fileLength = file.length();
    long bytesLeft = fileLength;
    int  read = 0;
    //Read our file into the md buffer
    while(bytesLeft > 0){
        try {
            read = reader.read(buffer,0, bytesLeft < buffer.length ? (int)bytesLeft : buffer.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        md.update(buffer,0,read);
        bytesLeft -= read;
    }
    byte[] digest = md.digest();
    for (int i = 0; i < digest.length;i++) {
        hexDigest += String.format("%02x" ,0xFF & digest[i]);
    }
    try {
        reader.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return hexDigest;
}
