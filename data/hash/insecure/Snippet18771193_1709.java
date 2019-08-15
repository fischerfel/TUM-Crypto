public class MD5 {

public static void main(String[] args) throws IOException {
    File file = new File("/Users/itaihay/Desktop/test");
    for (File f : file.listFiles()) {
        try {
            model.MD5.hash(f);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.

        }
    }

private static MessageDigest md;
private static BufferedInputStream fis;
private static byte[] dataBytes;
private static byte[] mdbytes;

private static void clean() throws NoSuchAlgorithmException {
    md = MessageDigest.getInstance("MD5");
    dataBytes = new byte[8192];
}
public static void hash(File file) {
    try {
        clean();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    try {
        fis = new BufferedInputStream(new FileInputStream(file));
        int nread = 0;
        while ((nread = fis.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nread);
        }
        nread = 0;
        mdbytes = md.digest();  System.out.println(javax.xml.bind.DatatypeConverter.printHexBinary(mdbytes).toLowerCase());

    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
            fis.close();
            dataBytes = null;
            md = null;
            mdbytes = null;
        } catch (IOException e) {
            e.printStackTrace();
      }       
    }
  }
}
