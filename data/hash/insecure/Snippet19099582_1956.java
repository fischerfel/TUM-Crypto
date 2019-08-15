FileInputStream FinputStream = hdfsDIS;   // <---This is where the problem is
MessageDigest md;
    try {
        md = MessageDigest.getInstance("MD5");  
        FileChannel channel = FinputStream.getChannel();
        ByteBuffer buff = ByteBuffer.allocate(2048);

        while(channel.read(buff) != -1){
            buff.flip();
            md.update(buff);
            buff.clear();
        }
        byte[] hashValue = md.digest();

        return toHex(hashValue);
    }
    catch (NoSuchAlgorithmException e){
        return null;
    } 
    catch (IOException e){
        return null;
    }
