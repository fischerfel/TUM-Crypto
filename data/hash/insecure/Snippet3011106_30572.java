MessageDigest md = MessageDigest.getInstance( "MD5" );
InputStream input = new FileInputStream( "file" );
byte[] buffer = new byte[1024];
int readLen;
while( ( readLen = input.read( buffer ) ) != -1 )
    md.update( buffer, 0, readLen );
return md.digest();
