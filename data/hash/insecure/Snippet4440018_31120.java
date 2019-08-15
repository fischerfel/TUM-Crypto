MessageDigest md = MessageDigest.getInstance("MD5");
InputStream is = new FileInputStream("C:\\Temp\\Small\\Movie.mp4"); // Size 700 MB

byte [] buffer = new byte [blockSize];
int numRead;
do 
{
 numRead = is.read(buffer);
 if (numRead > 0) 
 {
  md.update(buffer, 0, numRead);
 }
} while (numRead != -1);

byte[] digest = md.digest();
