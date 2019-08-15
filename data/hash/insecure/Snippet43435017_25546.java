private void save(byte [] bytes, File baseDir) throws NoSuchAlgorithmException, IOException {

    File file = File.createTempFile("upload_", ".tmp");
    try(OutputStream stream =  new FileOutputStream(file)){
        MessageDigest digest = MessageDigest.getInstance("md5");
        try(DigestOutputStream digestOutputStream = new DigestOutputStream(stream, digest)){
            digestOutputStream.write(bytes, 0, bytes.length);
            byte[] signature = digest.digest();
            String newName = md5ToStr(signature);

            File outFile = new File(baseDir,newName);
            if(outFile.exists()){
                // Already present;
                file.delete();
            }else{
                Files.move(file.toPath(), outFile.toPath(), REPLACE_EXISTING, ATOMIC_MOVE);
                // File saved
                // Save path reference and file name in DB
            }

        }
    }
}

private String md5ToStr(byte[] signature) {
    // TODO Auto-generated method stub
    String txt = new HexBinaryAdapter().marshal(signature);
    return txt;
}
