MessageDigest messageDigest = MessageDigest.getInstance("MD5");  
messageDigest.update(user.getPassword().getBytes(),0, user.getPassword().length());  
String hashedPass = new BigInteger(1,messageDigest.digest()).toString(16);  
if (hashedPass.length() < 32) {
   hashedPass = "0" + hashedPass; 
}
