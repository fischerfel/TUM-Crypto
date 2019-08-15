public static void main(String args[]) throws Exception{
       String str="test string";
       MessageDigest messageDigest=MessageDigest.getInstance("MD5");
       messageDigest.update(str.getBytes(),0,str.length());
       System.out.println("MD5: "+new BigInteger(1,messageDigest.digest()).toString(16));
}
