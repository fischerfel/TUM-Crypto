InputStream inputStream = hdfsDIS;
MessageDigest md;
try {
    md = MessageDigest.getInstance("MD5");  
    ReadableByteChannel channel = Channels.newChannel(inputStream);
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
