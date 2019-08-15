val md = MessageDigest.getInstance( hashInfo.algorithm )
val input = new FileInputStream( "file" )
val buffer = new Array[ Byte ]( 1024 )
var readLen = 0
while( readLen != -1 )
{
    readLen = input.read( buffer )
    if( readLen != -1 )
        md.update( buffer, 0, readLen )
}
md.digest
