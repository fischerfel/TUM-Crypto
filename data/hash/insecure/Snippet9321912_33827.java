private String generateMD5(SequenceInputStream inputStream){
    if(inputStream==null){
        return null;
    }
    MessageDigest md;
    try {
        int read =0;
        byte[] buf = new byte[2048];
        md = MessageDigest.getInstance("MD5");
        while((read = inputStream.read(buf))>0){
            md.update(buf,0,read);
        }
        byte[] hashValue = md.digest();
        return new String(hashValue);
    } catch (NoSuchAlgorithmException e) {
        return null;
    } catch (IOException e) {
        return null;
    }finally{
        try {
            if(inputStream!=null)inputStream.close();
        } catch (IOException e) {
            // ...
        }
    } 
