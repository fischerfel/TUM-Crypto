public static byte[] createChecksum(byte buffer[], int len){ 
     MessageDigest complete = MessageDigest.getInstance("MD5");
     complete.update(buffer,0,len);
     return complete.digest();
}
