    public String digest( ZipInputStream entry ) throws IOException{

        byte[] digest = null;
        MessageDigest md5 = null;
        String mdEnc = "";
        try {
            md5 = MessageDigest.getInstance( "MD5" );

            ZipEntry current;
            if( entry != null ) {
                while(( current = entry.getNextEntry() ) != null ) {
                    if( current.isDirectory() ) {
                        digest = this.encodeUTF8( current.getName() );
                        md5.update( digest );
                    }
                    else{
                        int size = ( int )current.getSize();
                        if(size > 0){
                            digest = new byte[ size ];
                            entry.read( digest, 0, size );
                            md5.update( digest );
                        }
                    }
                }
                digest = md5.digest();
                mdEnc = new BigInteger( 1, md5.digest() ).toString( 16 );
                entry.close();
            }
        }
        catch ( NoSuchAlgorithmException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mdEnc;
    }

         public byte[] encodeUTF8( String name ) {
             final Charset UTF8_CHARSET = Charset.forName( "UTF-8" );
                 return name.getBytes( UTF8_CHARSET );
         }
