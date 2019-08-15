 private byte[] calculateMD5ofFile(String location) throws IOException, NoSuchAlgorithmException {
        FileInputStream fs= new FileInputStream(location);
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] buffer=new byte[bufferSize];
        int bytes=0;
        do{
            bytes=fs.read(buffer,0,bufferSize);
            if(bytes>0)
                md.update(buffer,0,bytes);

        }while(bytes>0);
        byte[] Md5Sum = md.digest();
        return Md5Sum;
    }
