DigestInputStream dis = new DigestInputStream(is,
  MessageDigest.getInstance(SHA1));
BufferedInputStream bis = new BufferedInputStream(dis);
BufferedOutputStream bos = new BufferedOutputStream(
  new FileOutputStream(file.getName()));
while ((i = bis.read()) != -1) {
   bos.write(i);
}
bos.close();
return dis.getMessageDigest().digest();
