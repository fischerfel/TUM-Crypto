String str = "123456";
MessageDigest sha = MessageDigest.getInstance("SHA-1");
byte[] hash = sha.digest(str.getBytes());
for(int i =0 ; i<hash.length;i++){
   System.out.print((new Byte(hash[i]))+" ");
}
