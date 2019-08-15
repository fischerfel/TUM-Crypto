public String getMD5Hash(Object obj) throws IOException, NoSuchAlgorithmException {

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutput out = new ObjectOutputStream(bos);   
    out.writeObject(obj);
    byte[] data = bos.toByteArray();                
    MessageDigest m = MessageDigest.getInstance("MD5");                     
    m.update(data,0,data.length);
    BigInteger i = new BigInteger(1,m.digest());
    return String.format("%1$032X", i);     
}
