Cipher c = Cipher.getInstance( "DES" );
Key k = new SecretKeySpec( pass.getBytes(), "DES" );
c.init( Cipher.ENCRYPT_MODE, k );

OutputStream cos = new CipherOutputStream( out, c );
cos.write( bytes );
cos.close()
