//snippet1
byte[] byteArr = new byte[(int) uploadedFile.getLength()];
try {
 stream = new BufferedInputStream(uploadedFile.getInputStream());
 stream.read(byteArr);
 stream.close(); 
} catch (IOException e) {
 e.printStackTrace();
}
md = MessageDigest.getInstance("SHA-1"); 
byte[] sha1hash = new byte[40];
md.update(byteArr, 0, byteArr.length);
sha1hash = md.digest();

//snippet2
md = MessageDigest.getInstance("SHA-1");
InputStream is = uploadedFile.getInputStream();
try {
 is = new DigestInputStream(is, md);
} finally {
 try {
  is.close();
 } catch (IOException e) {
  e.printStackTrace();
 }
}
sha1hash = md.digest();
