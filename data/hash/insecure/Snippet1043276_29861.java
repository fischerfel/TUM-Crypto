try {
   String s = "TEST STRING";
   MessageDigest md5 = MessageDigest.getInstance("MD5");
   md5.update(s.getBytes(),0,s.length());
   String signature = new BigInteger(1,md5.digest()).toString(16);
   System.out.println("Signature: "+signature);

} catch (final NoSuchAlgorithmException e) {
   e.printStackTrace();
}
