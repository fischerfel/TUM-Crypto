  try {
        FileInputStream fis = new FileInputStream("upload.txt");                
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] buffer=new byte[8192];
        int read=0;
        while( (read = fis.read(buffer)) > 0)
                md.update(buffer, 0, read);
        byte[] md5 = md.digest();
        BigInteger bi=new BigInteger(1, md5);
        String output = bi.toString(16);
        System.out.println(output);
        SubmitFeed.submit(fis, output);
} catch(Exception e) { 
  // removed: not relevant to question
}
