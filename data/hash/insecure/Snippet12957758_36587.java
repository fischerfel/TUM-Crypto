PdfReader reader = new PdfReader("myfile.pdf");
MessageDigest messageDigest = MessageDigest.getInstance("MD5");
int pageCount = reader.getNumberOfPages(); 
for(int i=1;i <= pageCount; i++)
{
     byte[] buf = reader.getPageContent(i);
     messageDigest.update(buf, 0, buf.length);
}
byte[] hash = messageDigest.digest();
