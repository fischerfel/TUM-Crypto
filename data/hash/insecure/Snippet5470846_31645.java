MessageDigest md = MessageDigest.getInstance("MD5");

byte[] buffer = new byte[8*1024];
while( int read = input.read(buffer) > 0){
   md.update(buffer, 0, read);
}
byte[] hash = md.digest();
