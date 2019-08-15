    MessageDigest md = MessageDigest.getInstance("SHA1");
DigestInputStream din = new DigestInputStream(inputstream, md);
while(din.read(headerArr,0,8) != -1){
}
byte[]headerData = md.digest();
//skip some bytes
long skippedBytes = inputstream.skip(bytesTobeskipped);
