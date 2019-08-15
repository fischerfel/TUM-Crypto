MessageDigest md;
try {
    md = MessageDigest.getInstance("MD5");  
    byte[] buff = new byte[2048];
    int count;

    while((count = hdfsDIS.read(buff)) != -1){
        md.update(buff, 0, count);
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
