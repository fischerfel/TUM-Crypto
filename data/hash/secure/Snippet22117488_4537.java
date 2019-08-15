private MessageDigest fileHash(File file) {
    MessageDigest md=null;
    try {
    md = MessageDigest.getInstance("SHA-256");
    FileInputStream fileinputstream = new FileInputStream(file);
    byte[] dataBytes = new byte[1024];
    int nread = 0;
    while ((nread = fileinputstream.read(dataBytes)) != -1) {
        md.update(dataBytes, 0, nread);
    }
    }
    catch(IOException e) {
        e.printStackTrace();
    }
    catch(NoSuchAlgorithmException e){
        e.printStackTrace();
    }
    return md;
}
