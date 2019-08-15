  public class Utils {


public static final ThreadLocal<MessageDigest> mdT = new ThreadLocal<MessageDigest>(){
   protected MessageDigest initialValue(){
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  
        }
       return null;
   }
};

public static final ThreadLocal<ByteBuffer> bufferT = new ThreadLocal<ByteBuffer>(){
    protected ByteBuffer initialValue(){
            return ByteBuffer.allocate(32000);
    }
};

private static Utils util = new Utils();
private static MessageDigest md;
private static FileChannel fileChannel;
private static ByteBuffer buffer = bufferT.get();

private Utils() {

//            md = MessageDigest.getInstance("MD5");
        md = mdT.get();

}
public static String toMD5(File file) throws NoSuchAlgorithmException, IOException {
//        BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));

   RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");

    fileChannel = randomAccessFile.getChannel();

    /*while (fileChannel.read(buffer) != -1) {
        buffer.flip();
        md.update(buffer);
        buffer.clear();
    }*/

    while (fileChannel.read(bufferT.get()) != -1) {
        bufferT.get().flip();
        md.update(bufferT.get());
        bufferT.get().clear();
    }

    byte[] mdbytes = md.digest();

    randomAccessFile.close();
    bufferT.get().clear();
    mdT.get().reset();

    return javax.xml.bind.DatatypeConverter.printHexBinary(mdbytes)
            .toLowerCase();

}
