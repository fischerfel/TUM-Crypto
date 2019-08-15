MessageDigest md = MessageDigest.getInstance( "SHA" );
FileInputStream ios = new FileInputStream( "myfile.bmp" );
byte[] buffer = new byte[4 * 1024]; // what should this value be?
int read = 0;
while( ( read = ios.read( buffer ) ) > 0 )
    md.update( buffer, 0, read );
ios.close();
md.digest();
