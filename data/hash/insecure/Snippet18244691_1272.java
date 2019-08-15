private static ThreadLocal<MessageDigest> md = new ThreadLocal<MessageDigest>(){
     protected MessageDigest initialValue() {
         try {
             return MessageDigest.getInstance("MD5");
         } catch (NoSuchAlgorithmException e) {
             e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
         }
         System.out.println("Fail");
         return null;

     }
};


private static ThreadLocal<byte[]> dataBytes = new ThreadLocal<byte[]>(){

    protected byte[] initialValue(){
     return new byte[1024];
    }

};

public static String toMD5(File file) throws IOException, NoSuchAlgorithmException {

    //        MessageDigest mds = md.get();
    BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));


    //        byte[] dataBytes = new byte[1024];

    int nread = 0;
    while ((nread = fis.read(dataBytes.get())) != -1) {
        md.get().update(dataBytes.get(), 0, nread);
    }

    byte[] mdbytes = md.get().digest();

    //convert the byte to hex format method 2
    StringBuffer hexString = new StringBuffer();
    fis.close();
    System.gc();
    return javax.xml.bind.DatatypeConverter.printHexBinary(mdbytes).toLowerCase();




     //        return MD5.asHex(MD5.getHash(file));
}
