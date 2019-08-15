private String getMD5Hash(byte[] bytes) throws java.lang.Exception{
   String s="This is a test";
   MessageDigest m=MessageDigest.getInstance("MD5");
   m.update(bytes,0,bytes.length);
   return new BigInteger(1,m.digest()).toString(16);
}
