public class FileHashCompare {
    public static byte[] createByteArrayFromFile(File f) throws IOException{
        FileInputStream fis = new FileInputStream(f);
        long length = f.length();
        if (length>Integer.MAX_VALUE){
            throw new IllegalArgumentException("file too large to read");
        }
        byte[] buffer = new byte[(int)length];
        int offset = 0;
        int bytesRead = 0;
        while((offset<buffer.length) && ((bytesRead=fis.read(buffer,offset, buffer.length-offset)) >=0)  ){
             offset += bytesRead;
        }
        if (offset < buffer.length) {
            throw new IOException("Could not completely read file "+f.getName());
        }
        fis.close();
        return buffer;
    }
    public static String makeHashOfFile(File f) throws NoSuchAlgorithmException, IOException{
        String hashStr = null;
        byte[] bytes = createByteArrayFromFile(f);

        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.reset();
        md.update(bytes);
        byte[] hash = md.digest();
        hashStr = new String(hash);
        return hashStr;
    }

    public static boolean sameFile(File f1,File f2) throws NoSuchAlgorithmException, IOException{
        String hash1 =  makeHashOfFile(f1);
        String hash2 =  makeHashOfFile(f2);
        if (hash1.equals(hash2)){
            return true;
        }else{
            return false;
        }
    }

    public static void main(String[] args) {        
        long t1 = System.currentTimeMillis();
        try{
            File f1 = new File("/home/me/Pictures/painting-bob-ross-landscape-painting-1-21.jpg");
            //File f2 = new File("/home/me/Pictures/painting-bob-ross-landscape-painting-1-21 (copy).jpg");
            File f3 = new File("/home/me/Pictures/chainsaw1.jpeg");
            System.out.println("same file="+sameFile(f1,f3));
            long t2 = System.currentTimeMillis();
            System.out.println("time taken="+(t2-t1)+" millis");
        }catch(Exception e){
            e.printStackTrace();
        }

    }

}
