MessageDigest sha = MessageDigest.getInstance("SHA-1");
MessageDigest md5 = MessageDigest.getInstance("MD5");
InputStream input = ...;
byte[] buffer = new byte[BUFFER_SIZE];
int len;
while((len = input.read(buffer)) >= 0)
{
    sha.update(buffer,0,len);
    md5.update(buffer,0,len);
    ...
}
byte[] shaDigest = sha.digest();
byte[] md5Digest = md5.digest();
