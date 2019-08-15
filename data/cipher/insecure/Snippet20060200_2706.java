static ReadableByteChannel readChannel = null;
static WritableByteChannel writeChannel = null;
static SecretKey key = makeKeyFromPassword("chuj".getBytes());

public static SecretKey makeKeyFromPassword(byte[] password) {

    try {
        key = KeyGenerator.getInstance("DES").generateKey();
        byte[] encoded = key.getEncoded();
        return new SecretKeySpec(encoded, "DES");
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    return null;
}

public static void run(int mode) throws Exception {
    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
    ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

    //initializing cipher...
    Cipher cipher = javax.crypto.Cipher.getInstance("DES");                                                         
    cipher.init(mode, key);  

    int read = -1;
    while((read = readChannel.read(readBuffer)) != -1){
        readBuffer.flip();
        cipher.doFinal(readBuffer, writeBuffer);
        writeChannel.write(writeBuffer);
        readBuffer.clear();
        writeBuffer.clear();
    }
}

public static void main(String[] args) {
    // TODO Auto-generated method stub\
    FileOutputStream fos = null;
    String inFileString = "C:\\test.txt"; // Valid file pathname
    String fileString = "C:\\des.txt"; // Valid file pathname
    int mode = Cipher.ENCRYPT_MODE;
    FileSystem fs = FileSystems.getDefault();
    Path fp = fs.getPath(inFileString);

    try {
        readChannel = FileChannel.open(fp, EnumSet.of(StandardOpenOption.READ));
        fos = new FileOutputStream(fileString);
        writeChannel = Channels.newChannel(fos);
        run(mode);
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

}
